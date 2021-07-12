package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.comment.StoreComment;
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
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        String queryFromAndWhere = "FROM theme_comment c INNER JOIN member m ON m.member_id = c.member_id AND c.is_hidden = 0";

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
        int page = commentReqDto.getPage();
        int size = commentReqDto.getSize();
        boolean hasNext = total > page * size;

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

        return CommentListResDto.builder().comments(commentDtos)
                .page(page)
                .size(size)
                .total(total)
                .hasNext(hasNext)
                .build();
    }

    private void checkAlreadyExist(ThemeCommentDto commentDto){
        Theme theme = Theme.builder().id(commentDto.getThemeId()).build();
        Member member = authenticationUtil.getAuthUserEntity();
        Optional<ThemeComment> themeCommentExist = themeCommentRepository.findByThemeAndMember(theme, member);
        themeCommentExist.ifPresent((storeComment) -> {
            throw new AlreadyExistResourceException(
                    String.format("테마 아이디 [%d]에 대한 후기가 이미 존재합니다.", commentDto.getThemeId()));
        });
    }

    public ThemeCommentResDto addComment(ThemeCommentDto commentDto){
        checkAlreadyExist(commentDto);

        ThemeComment comment = commentMapper.mapDtoToEntity(commentDto, ThemeComment.class);
        Member member = authenticationUtil.getAuthUserEntity();
        Theme theme = Theme.builder().id(commentDto.getThemeId()).build();
        comment.setMember(member);
        comment.setTheme(theme);
        comment.setActive(commentDto.isActiveSet());
        comment.setHorror(commentDto.isHorrorSet());
        comment = themeCommentRepository.save(comment);

        if(commentDto.getUploadFiles().length > 0){
            List<Long> ids = Arrays.stream(commentDto.getUploadFiles()).map(file -> file.getId()).collect(Collectors.toList());
            fileService.updateReferIds(ids, comment.getId());
        }

        ThemeCommentResDto resDto = commentMapper.mapEntityToDto(comment, ThemeCommentResDto.class);
        return resDto;
    }

    private boolean hasAuthority(long id) {
        if(!authenticationUtil.isSameUser(id)) {
            throw new UnAuthorizedException(String.format("user has no authority on resource id %d", id));
        }
        return true;
    }

    public ThemeCommentResDto updateComment(long id, ThemeCommentDto modifyReqDto){
        ThemeComment comment = getCommentByIdIfExist(id);
        hasAuthority(comment.getMember().getId());

        modelMapper.map(modifyReqDto, comment);
        comment.setHorror(modifyReqDto.isHorrorSet());
        comment.setActive(modifyReqDto.isActiveSet());
        ThemeComment commentUpdated = themeCommentRepository.save(comment);
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

        return CommentListResDto.builder()
                .total(commentPage.getTotalElements())
                .comments(mapStoreCommentsToDtos(themeComments))
                .size(commentPage.getSize())
                .hasNext(commentPage.hasNext())
                .page(reqDto.getPage())
                .build();
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
