package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.comment.CommentRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthenticationUtil authenticationUtil;
    private final EntityManager em;

    public CommentResDto addComment(CommentModifyReqDto commentModifyReqDto, Function<CommentModifyReqDto, CommentResDto> addFunc){
        CommentDto parComment = commentModifyReqDto.getParComment();
        boolean hasParComment = parComment != null;
        if(hasParComment){
            commentRepository.updateBelowCommentSeq(parComment.getId(), parComment.getSeq());
        }
        return addFunc.apply(commentModifyReqDto);
    }
    
    public CommentListResDto getCommentList(CommentReqDto commentReqDto) {

        // 로그인한 경우 유저가 해당 comment를 좋아요 했는지 여부 확인한다.
        String querySelectIsGoodChk = ", (SELECT 0) as is_good_chk ";
        if(authenticationUtil.isAuthenticated()){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsGoodChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, 0)) FROM good WHERE gtype= :type AND refer_id = c.comment_id AND is_good = 1) as is_good_chk ";
        }

        // 대댓글이 달릴 수 있는 comment의 경우 삭제된 댓글을 배제하지 않고 가져온다.
        String quweryWhereExcludeDeleteWhenHasRecomment = "";
        if(commentReqDto.getType().hasRecomment()){
            quweryWhereExcludeDeleteWhenHasRecomment = "ADN c.is_deleted = 0 ";
        }

        // from, where절
        String queryFromAndWhere = "FROM comment c INNER JOIN member m ON m.member_id = c.member_id WHERE c.ctype = :type"
                +quweryWhereExcludeDeleteWhenHasRecomment;

        String listQuery =  "SELECT c.*, m.nickname "+
                            ", (SELECT COUNT(*) FROM good WHERE gtype= :type AND refer_id = c.comment_id AND is_good = 1) as good_cnt " +
                            querySelectIsGoodChk +
                            queryFromAndWhere +
                            "ORDER BY par_id DESC, seq ASC, comment_id desc";

        PageRequest pageRequest = commentReqDto.getPageable();
        List<Object[]> results = em.createNativeQuery(listQuery, "commentResultMap")
                .setParameter("type", commentReqDto.getType().getChildEntityDiscriminatorValue())
                .setFirstResult(pageRequest.getPageNumber())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        String pagingQuery = "SELECT count(*) " + queryFromAndWhere;

        BigInteger totalCount = (BigInteger) em.createNativeQuery(pagingQuery)
                .setParameter("type", commentReqDto.getType().getChildEntityDiscriminatorValue())
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

    public long deleteComment(long id){
        Comment comment = getCommentByIdIfExist(id);
        comment.setDeleted(true);
        comment.setDeleteDate(LocalDateTime.now());
        commentRepository.save(comment);
        return comment.getId();
    }

    public CommentResDto updateComment(long id, CommentModifyReqDto modifyReqDto){
        Comment comment = getCommentByIdIfExist(id);
        comment.setContent(modifyReqDto.getContent());

        comment.setStar(modifyReqDto.getStarRate());
        Comment commentUpdated = commentRepository.save(comment);
        return commentMapper.mapEntityToDto(commentUpdated, CommentResDto.class);
    }

    public CommentResDto toggleHideComment(long id, boolean isHidden){
        Comment comment = getCommentByIdIfExist(id);
        comment.setHidden(isHidden);
        comment = commentRepository.save(comment);

        CommentResDto resDto = commentMapper.mapEntityToDto(comment, CommentResDto.class);
        return resDto;
    }

    public CommentDto getComment(long id){
        return commentMapper.mapEntityToDto(getCommentByIdIfExist(id), CommentDto.class);
    }

    private Comment getCommentByIdIfExist(long id){
        Optional<Comment> commentOpt = getCommentById(id);
        return getCommentIfExist(commentOpt, id);
    }

    private Optional<Comment> getCommentById(long id){
        Optional<Comment> commentOpt = commentRepository.findById(id);
        return commentOpt;
    }

    private Comment getCommentIfExist(Optional<Comment> commentOpt, long id) {
        Comment comment = commentOpt.orElseThrow( () ->
                new NoSuchResourceException(
                        String.format("%d와 일치하는 댓글이 존재하지 않습니다.", id)));
        return comment;
    }
}
