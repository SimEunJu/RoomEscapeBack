package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.comment.theme.*;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.entity.file.ThemeCommentFile;
import com.sej.escape.error.exception.AlreadyExistResourceException;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.error.exception.security.UnAuthorizedException;
import com.sej.escape.repository.comment.ThemeCommentRepository;
import com.sej.escape.service.file.FileService;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeCommentService {

    private final ThemeCommentRepository themeCommentRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final AuthenticationUtil authenticationUtil;
    private final CommentMapper commentMapper;
    private final EntityManager em;

    public CommentListResDto getCommentList(CommentReqDto commentReqDto) {

        // 로그인한 경우 유저가 해당 comment를 좋아요 했는지 여부 확인한다.
        String querySelectIsGoodChk = ", (SELECT 0) as is_good_chk ";
        if(authenticationUtil.isAuthenticated()){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsGoodChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, 0)) FROM good WHERE gtype= :gtype AND refer_id = c.theme_comment_id AND is_good = 1) as is_good_chk ";
        }

        // from, where절
        String queryFromAndWhere = "FROM theme_comment c INNER JOIN member m ON m.member_id = c.member_id AND c.is_hidden = 0 AND c.is_deleted = 0 ";

        String listQuery =  "SELECT c.*, m.nickname, m.member_id "+
                ", (SELECT COUNT(*) FROM good WHERE gtype= :gtype AND refer_id = c.theme_comment_id AND is_good = 1) as good_cnt " +
                querySelectIsGoodChk +
                queryFromAndWhere +
                "ORDER BY c.theme_comment_id desc";

        PageRequest pageRequest = commentReqDto.getPageable();
        List<Object[]> results = em.createNativeQuery(listQuery, "themeCommentResultMap")
                .setParameter("gtype", commentReqDto.getType().getGoodEntityDiscVal())
                .setFirstResult(pageRequest.getPageNumber())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        String pagingQuery = "SELECT count(*) " + queryFromAndWhere;

        BigInteger totalCount = (BigInteger) em.createNativeQuery(pagingQuery)
                .getSingleResult();

        int total = totalCount.intValue();
        boolean hasNext = total > commentReqDto.getTotal();

        List<ThemeCommentForListDto> commentDtos = results.stream().map(row -> {
            ThemeComment comment = (ThemeComment) row[0];
            ThemeCommentForListDto commentDto = commentMapper.mapEntityToDto(comment, ThemeCommentForListDto.class);
            commentDto.setContent(comment.getReview());

            String writerNickname = (String) row[1];
            long writerId = ((BigInteger) row[2]).longValue();
            commentDto.setWriter(writerNickname);
            commentDto.setWriterId(writerId);

            int goodCnt = ((BigInteger) row[3]).intValue();

            boolean isGoodChk = row[4] != null && ((BigInteger) row[4]).intValue() > 0;
            commentDto.setGoodChecked(isGoodChk);
            if(authenticationUtil.isAuthenticated()){
                goodCnt = isGoodChk ? goodCnt - 1 : goodCnt;
            }
            commentDto.setGood(goodCnt);

            return commentDto;
        }).collect(Collectors.toList());

        CommentListResDto resDto = new CommentListResDto();
        resDto.setTargetList(commentDtos);
        resDto.setPage(commentReqDto.getNextPage());
        resDto.setSize(commentReqDto.getSize());
        resDto.setTotal(total);
        resDto.setHasNext(hasNext);

        return resDto;
    }

    private void checkAlreadyExist(ThemeCommentDto commentDto){
        Theme theme = Theme.builder().id(commentDto.getThemeId()).build();
        Member member = authenticationUtil.getAuthUserEntity();

        Optional<ThemeComment> themeCommentExist = null;
        try{
            themeCommentExist = themeCommentRepository.findByThemeAndMemberAndIsDeletedFalse(theme, member);
        }catch (NonUniqueResultException e){
            throwAlreadyExistException(commentDto.getThemeId());
        }

        themeCommentExist.ifPresent((storeComment) -> throwAlreadyExistException(commentDto.getThemeId()));
    }

    private void throwAlreadyExistException(long themeId){
        throw new AlreadyExistResourceException(
                String.format("테마 아이디 [%d]에 대한 후기가 이미 존재합니다.", themeId));
    }

    private void uploadFiles(FileResDto[] files, long referId){
        List<Long> ids = Arrays.stream(files).map(file -> file.getId()).collect(Collectors.toList());
        fileService.updateReferIds(ids, referId);
    }

    public ThemeCommentResDto addComment(ThemeCommentDto commentDto){
        checkAlreadyExist(commentDto);

        ThemeComment comment = commentMapper.mapDtoToEntity(commentDto, ThemeComment.class);
        Member member = authenticationUtil.getAuthUserEntity();
        Theme theme = Theme.builder().id(commentDto.getThemeId()).build();

        comment.setMember(member);
        comment.setTheme(theme);
        comment = themeCommentRepository.save(comment);

        if(commentDto.getUploadFiles() != null && commentDto.getUploadFiles().length > 0){
            uploadFiles(commentDto.getUploadFiles(), comment.getId());
        }

        ThemeCommentResDto resDto = commentMapper.mapEntityToDto(comment, ThemeCommentResDto.class);

        return resDto;
    }

    private boolean hasAuthority(long id) {
        if(!authenticationUtil.isSameUser(id)) {
            throw new AccessDeniedException(String.format("user has no authority on resource id %d", id));
        }
        return true;
    }

    @Transactional
    public ThemeCommentResDto updateComment(long id, ThemeCommentDto modifyReqDto){
        ThemeComment comment = getCommentByIdIfExist(id);
        hasAuthority(comment.getMember().getId());

        modelMapper.map(modifyReqDto, comment);

        ThemeComment commentUpdated = themeCommentRepository.save(comment);

        if(modifyReqDto.getUploadFiles() != null && modifyReqDto.getUploadFiles().length > 0){
            uploadFiles(modifyReqDto.getUploadFiles(), comment.getId());
        }

        return commentMapper.mapEntityToDto(commentUpdated, ThemeCommentResDto.class);
    }

    public ThemeCommentDetailDto getComment(long id){
        Optional<Object> commentOpt = themeCommentRepository.findDetailById(id);
        Object[] comment = (Object[]) getResultIfExist(commentOpt, id);
        ThemeComment themeComment = (ThemeComment) comment[0];
        ThemeCommentFile themeCommentFile = (ThemeCommentFile) comment[1];

        ThemeCommentDetailDto detailDto = commentMapper.mapEntityToDto(themeComment, ThemeCommentDetailDto.class);
        detailDto.setTheme(commentMapper.mapDtoToEntity(themeComment.getTheme(), Ancestor.class));
        detailDto.setStore(commentMapper.mapDtoToEntity(themeComment.getTheme().getStore(), Ancestor.class));
        detailDto.setWriter(themeComment.getMember().getNickname());

        if(themeCommentFile != null) {
            detailDto.setUploadFiles(
                    new FileResDto[]{
                            commentMapper.mapEntityToDto(themeCommentFile, FileResDto.class)
                    });
        }

        return detailDto;
    }

    private ThemeComment getCommentByIdIfExist(long id){
        Optional<ThemeComment> commentOpt = getCommentById(id);
        return getResultIfExist(commentOpt, id);
    }

    private Optional<ThemeComment> getCommentById(long id){
        Optional<ThemeComment> commentOpt = themeCommentRepository.findById(id);
        return commentOpt;
    }

    private <T> T getResultIfExist(Optional<T> commentOpt, long id) {
        T comment = commentOpt.orElseThrow( () ->
                new NoSuchResourceException(
                        String.format("%d와 일치하는 댓글이 존재하지 않습니다.", id)));
        return comment;
    }

    public CommentListResDto getCommentsByMember(CommentListReqDto reqDto){

        Sort sort = reqDto.getOrder().getSort();
        Pageable pageable = reqDto.getPageable(sort);
        Member member = authenticationUtil.getAuthUserEntity();

        Page<ThemeComment> commentPage = themeCommentRepository.findAllByMember(pageable, member);
        List<ThemeComment> themeComments = commentPage.getContent();

        CommentListResDto resDto = new CommentListResDto();
        resDto.setPageResult(commentPage);
        resDto.setTargetList(mapStoreCommentsToDtos(themeComments));

        return resDto;
    }

    private List<ThemeCommentForListByMemberDto> mapStoreCommentsToDtos(List<ThemeComment> entitis){
        return entitis.stream().map(themeComment -> {

            Theme theme = themeComment.getTheme();

            ThemeCommentForListByMemberDto dto = commentMapper.mapEntityToDto(themeComment, ThemeCommentForListByMemberDto.class);
            dto.setName(theme.getName());
            dto.setThemeId(theme.getId());

            Member member = themeComment.getMember();
            dto.setWriter(member.getNickname());

            return dto;
        }).collect(Collectors.toList());
    }

    public long deleteComment(long id){
        ThemeComment comment = getCommentByIdIfExist(id);
        hasAuthority(comment.getMember().getId());

        comment.setDeleted(true);
        comment.setDeleteDate(LocalDateTime.now());
        themeCommentRepository.save(comment);
        return comment.getId();
    }

    public ThemeCommentResDto toggleHideComment(long id, boolean isHidden){
        ThemeComment comment = getCommentByIdIfExist(id);
        hasAuthority(comment.getMember().getId());

        comment.setHidden(isHidden);
        comment = themeCommentRepository.save(comment);

        ThemeCommentResDto resDto = commentMapper.mapEntityToDto(comment, ThemeCommentResDto.class);
        return resDto;
    }

    public List<ThemeCommentForListByMemberDto> readTopComments(){

        List<ThemeComment> comments = themeCommentRepository.findTopComments();
        List<ThemeCommentForListByMemberDto> commentDtos = mapStoreCommentsToDtos(comments);

        return commentDtos;
    }

    public List<ThemeCommentForListByMemberDto> readLatestComments(PageReqDto pageReqDto){

        Pageable pageable = pageReqDto.getPageable(Sort.by("regDate").descending());

        Page<ThemeComment> comments = themeCommentRepository.findLatestComments(pageable);

        List<ThemeCommentForListByMemberDto> commentDtos = mapStoreCommentsToDtos(comments.getContent());

        return commentDtos;
    }


}
