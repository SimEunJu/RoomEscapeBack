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
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthenticationUtil authenticationUtil;
    private final EntityManager em;

    private long updateBelowCommentSeq(long parCommentId, int parCommentSeq){
        return commentRepository.updateBelowCommentSeq(parCommentId, parCommentSeq);
    }

    // TODO: 추후 enum으로 매핑 후 get
    private String getTypeFlag(String type){
        return type.substring(0, 1).toUpperCase();
    }

    public CommentResDto addComment(CommentModifyReqDto commentModifyReqDto, Function<CommentModifyReqDto, CommentResDto> addFunc){
        CommentDto parComment = commentModifyReqDto.getParComment();
        boolean hasParComment = parComment != null;
        if(hasParComment){
            commentRepository.updateBelowCommentSeq(parComment.getId(), parComment.getSeq());
        }
        return addFunc.apply(commentModifyReqDto);
    }
    
    public CommentListResDto getCommentList(CommentReqDto commentReqDto) {
        String type = getTypeFlag( commentReqDto.getType() );

        PageRequest pageRequest = commentReqDto.getPageable();

        String querySelectIsGoodChk = ", (SELECT 0) as is_good_chk ";
        boolean isAuthenticated = authenticationUtil.isAuthenticated();
        if(isAuthenticated){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsGoodChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, 0)) FROM good WHERE gtype= :type AND refer_id = c.comment_id AND is_good = 1) as is_good_chk ";
        }

        String queryFromAndWhere = "FROM comment c INNER JOIN member m ON m.member_id = c.member_id WHERE c.ctype = :type AND c.comment_id IN ( "+
                "SELECT comment_id FROM comment ic WHERE ic.refer_id = :referId AND ic.depth = 0 ORDER BY comment_id desc ) ";

        String listQuery =  "SELECT c.*, m.nickname "+
                            ", (SELECT COUNT(*) FROM good WHERE gtype='S' AND refer_id = c.comment_id AND is_good = 1) as good_cnt " +
                            querySelectIsGoodChk +
                            queryFromAndWhere +
                            "ORDER BY par_id DESC, seq ASC, comment_id desc";

        List<Object[]> results = em.createNativeQuery(listQuery, "storeCommentResultMap")
                .setParameter("type", type)
                .setParameter("referId", commentReqDto.getReferId())
                .setFirstResult(pageRequest.getPageNumber())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        String pagingQuery = "SELECT count(*) " + queryFromAndWhere;

        BigInteger totalCount = (BigInteger) em.createNativeQuery(pagingQuery)
                .setParameter("type", type)
                .setParameter("referId", commentReqDto.getReferId())
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
            if(isAuthenticated){
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
