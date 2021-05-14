package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.entity.file.ThemeCommentFile;
import com.sej.escape.error.exception.NoSuchResourceException;
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
            querySelectIsGoodChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, 0)) FROM good WHERE gtype= :type AND refer_id = c.theme_comment_id AND is_good = 1) as is_good_chk ";
        }

        // 대댓글이 달릴 수 있는 comment의 경우 삭제된 댓글을 배제하지 않고 가져온다.
        String quweryWhereExcludeDeleteWhenHasRecomment = "";
        if(commentReqDto.getType().hasRecomment()){
            quweryWhereExcludeDeleteWhenHasRecomment = "ADN c.is_deleted = 0 ";
        }

        // from, where절
        String queryFromAndWhere = "FROM theme_comment c INNER JOIN member m ON m.member_id = c.member_id "
                +quweryWhereExcludeDeleteWhenHasRecomment;

        String listQuery =  "SELECT c.*, m.nickname "+
                ", (SELECT COUNT(*) FROM good WHERE gtype= :type AND refer_id = c.comment_id AND is_good = 1) as good_cnt " +
                querySelectIsGoodChk +
                queryFromAndWhere +
                "ORDER BY par_id DESC, seq ASC, comment_id desc";

        PageRequest pageRequest = commentReqDto.getPageable();
        List<Object[]> results = em.createNativeQuery(listQuery, "commentResultMap")
                .setParameter("type", commentReqDto.getType().getEntityDiscriminatorValue())
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

        List<CommentDto> commentDtos = results.stream().map(row -> {
            StoreComment comment = (StoreComment) row[0];
            CommentDto commentDto = commentMapper.mapEntityToDto(comment, CommentDto.class);
            if(comment.isDeleted()) commentDto.setContent("삭제된 댓글입니다.");

            String nickname = (String) row[1];
            commentDto.setWriter(nickname);

            int goodCnt = ((BigInteger) row[2]).intValue();

            boolean isGoodChk = row[3] != null && ((BigInteger) row[3]).intValue() > 0;
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

    public ThemeCommentResDto addComment(ThemeCommentDto commentDto){
        ThemeComment comment = commentMapper.mapDtoToEntity(commentDto, ThemeComment.class);
        Member member = authenticationUtil.getAuthUserEntity();
        Theme theme = Theme.builder().id(commentDto.getThemeId()).build();
        comment.setMember(member);
        comment.setTheme(theme);
        comment.setActive(commentDto.isActive());
        comment.setHorror(commentDto.isHorror());
        comment = themeCommentRepository.save(comment);

        if(commentDto.getUploadFiles().length > 0){
            List<Long> ids = Arrays.stream(commentDto.getUploadFiles()).map(file -> file.getId()).collect(Collectors.toList());
            fileService.updateReferIds(ids, comment.getId());
        }

        ThemeCommentResDto resDto = commentMapper.mapEntityToDto(comment, ThemeCommentResDto.class);
        return resDto;
    }

    public ThemeCommentResDto updateComment(long id, ThemeCommentDto modifyReqDto){
        ThemeComment comment = getCommentByIdIfExist(id);
        modelMapper.map(modifyReqDto, comment);
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

        Page<Object[]> commentPage = themeCommentRepository.findAllByMember(pageable, member);
        List<Object[]> themeComments = commentPage.getContent();
        return CommentListResDto.builder()
                .total(commentPage.getTotalElements())
                .comments(mapStoreCommentsToDtos(themeComments))
                .size(commentPage.getSize())
                .hasNext(commentPage.hasNext())
                .page(reqDto.getPage())
                .build();
    }

    private List<ThemeCommentForListDto> mapStoreCommentsToDtos(List<Object[]> entitis){
        return entitis.stream().map(e -> {

            ThemeComment themeComment = (ThemeComment) e[0];
            Theme theme = (Theme) e[1];

            ThemeCommentForListDto dto = commentMapper.mapEntityToDto(themeComment, ThemeCommentForListDto.class);
            dto.setName(theme.getName());
            dto.setThemeId(theme.getId());

            return dto;
        }).collect(Collectors.toList());
    }

    public List<CommentDto> readTopComments(PageReqDto pageReqDto){
        /*
        메인페이지 최대 10개
        좋아요 + 최신순
        */
        Sort sort = Sort.by(Sort.Direction.DESC, "good", "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);

        LocalDateTime aWeekAgo = LocalDateTime.now().minusWeeks(1);

        Page<ThemeComment> comments = themeCommentRepository.findTopComments(aWeekAgo, pageable);

        List<CommentDto> commentDtos = commentMapper.mapEntitesToDtos(comments.getContent(), CommentDto.class);

        return commentDtos;
    }

    public List<CommentDto> readLatestComments(PageReqDto pageReqDto){

        Pageable pageable = pageReqDto.getPageable(Sort.by("regDate").descending());

        Page<ThemeComment> comments = themeCommentRepository.findLatestComments(pageable);

        List<CommentDto> commentDtos = commentMapper.mapEntitesToDtos(comments.getContent(), CommentDto.class);

        return commentDtos;
    }


}
