package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.error.exception.security.UnAuthorizedException;
import com.sej.escape.repository.comment.CommentRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
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
            querySelectIsGoodChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, 0)) FROM good WHERE gtype= :gtype AND refer_id = c.comment_id AND is_good = 1) as is_good_chk ";
        }

        // 대댓글이 달릴 수 있는 comment의 경우 삭제된 댓글을 배제하지 않고 가져온다.
        String quweryWhereExcludeDeleteWhenHasRecomment = "";
        if(!commentReqDto.getType().hasRecomment()){
            quweryWhereExcludeDeleteWhenHasRecomment = "AND c.is_deleted = 0 ";
        }

        // from, where절
        String queryFromAndWhere = "FROM comment c INNER JOIN member m ON m.member_id = c.member_id WHERE c.ctype = :ctype AND c.is_hidden = 0 "
                +quweryWhereExcludeDeleteWhenHasRecomment;

        String listQuery =  "SELECT c.*, m.nickname, m.member_id "+
                            ", (SELECT COUNT(*) FROM good WHERE gtype= :gtype AND refer_id = c.comment_id AND is_good = 1) as good_cnt " +
                            querySelectIsGoodChk +
                            queryFromAndWhere +
                            "ORDER BY par_id DESC, seq ASC, comment_id desc";

        PageRequest pageRequest = commentReqDto.getPageable();
        List<Object[]> results = em.createNativeQuery(listQuery, "commentResultMap")
                .setParameter("ctype", commentReqDto.getType().getCommentEntityDiscVal())
                .setParameter("gtype", commentReqDto.getType().getGoodEntityDiscVal())
                .setFirstResult(pageRequest.getPageNumber())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        String pagingQuery = "SELECT count(*) " + queryFromAndWhere;

        BigInteger totalCount = (BigInteger) em.createNativeQuery(pagingQuery)
                .setParameter("ctype", commentReqDto.getType().getCommentEntityDiscVal())
                .getSingleResult();

        int total = totalCount.intValue();
        int page = commentReqDto.getPage();
        int size = commentReqDto.getSize();
        boolean hasNext = total > page * size;

        List<CommentDto> commentDtos = results.stream().map(row -> {
            Comment comment = (Comment) row[0];
            CommentDto commentDto = commentMapper.mapEntityToDto(comment, CommentDto.class);
            if(comment.isDeleted()) commentDto.setContent("삭제된 댓글입니다.");

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
        resDto.setPage(page);
        resDto.setSize(size);
        resDto.setTotal(total);
        resDto.setHasNext(hasNext);

        return resDto;
    }

    private boolean hasAuthority(long id) {
        if(!authenticationUtil.isSameUser(id)) {
            throw new UnAuthorizedException(String.format("user has no authority on resource id %d", id));
        }
        return true;
    }

    public long deleteComment(long id){
        Comment comment = getCommentByIdIfExist(id);
        hasAuthority(comment.getMember().getId());

        comment.setDeleted(true);
        comment.setDeleteDate(LocalDateTime.now());
        commentRepository.save(comment);
        return comment.getId();
    }

    public CommentResDto updateComment(long id, CommentModifyReqDto modifyReqDto){

        Comment comment = getCommentByIdIfExist(id);

        hasAuthority(comment.getMember().getId());

        comment.setContent(modifyReqDto.getContent());
        comment.setStar(modifyReqDto.getStarRate());
        comment = commentRepository.save(comment);

        return commentMapper.mapEntityToDto(comment, CommentResDto.class);
    }

    public CommentResDto toggleHideComment(long id, boolean isHidden){
        Comment comment = getCommentByIdIfExist(id);
        hasAuthority(comment.getMember().getId());

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
