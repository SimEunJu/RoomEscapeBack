package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentModifyReqDto;
import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.dto.comment.CommentResDto;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private long updateBelowCommentSeq(long parCommentId, int parCommentSeq){
        return commentRepository.updateBelowCommentSeq(parCommentId, parCommentSeq);
    }

    public CommentResDto addComment(CommentModifyReqDto commentModifyReqDto, Function<CommentModifyReqDto, CommentResDto> addFunc){
        CommentDto parComment = commentModifyReqDto.getParComment();
        boolean hasParComment = parComment != null;
        if(hasParComment){
            commentRepository.updateBelowCommentSeq(parComment.getId(), parComment.getSeq());
        }
        return addFunc.apply(commentModifyReqDto);
    }

    public List<CommentDto> getCommentList(CommentReqDto commentReqDto) {
        PageRequest pageRequest = commentReqDto.getPageable();
        List<Comment> comments = commentRepository.findAllByPaging(
                commentReqDto.getReferId(),
                commentReqDto.getType(),
                pageRequest.getPageNumber(),
                pageRequest.getPageSize()
        );
        return commentMapper.mapEntitesToDtos(comments);
    }

    public long reportComment(long id){
        Comment comment = getCommentByIdIfExist(id);
        comment.setReport(comment.getReport() + 1);
        commentRepository.save(comment);
        return comment.getId();
    }

    public long deleteComment(long id){
        Comment comment = getCommentByIdIfExist(id);
        comment.setDeleted(true);
        comment.setDeleteDate(LocalDateTime.now());
        commentRepository.save(comment);
        return comment.getId();
    }

    public CommentDto updateComment(long id, CommentModifyReqDto modifyReqDto){
        Comment comment = getCommentByIdIfExist(id);
        comment.setContent(modifyReqDto.getContent());

        comment.setStar(modifyReqDto.getStarRate());
        Comment commentUpdated = commentRepository.save(comment);
        return commentMapper.mapEntityToDto(commentUpdated);
    }

    public long toggleLikeComment(long id, boolean isLikeSet){
        Comment comment = getCommentByIdIfExist(id);
        int diff = isLikeSet ? 1 : -1;
        comment.setGood(comment.getGood() + diff);
        commentRepository.save(comment);
        return comment.getId();
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
