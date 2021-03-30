package com.sej.escape.service.comment;

import com.sej.escape.dto.CommentDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.comment.CommentRepository;
import com.sej.escape.repository.comment.ThemeCommentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public void reportComment(long id){
        Comment comment = getCommentByIdIfExist(id);
        comment.setReport(comment.getReport() + 1);
        commentRepository.save(comment);
    }

    public void deleteComment(long id){
        Comment comment = getCommentByIdIfExist(id);
        comment.setDeleted(true);
        comment.setDeleteDate(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public CommentDto updateComment(long id, CommentDto commentDto){
        Comment comment = getCommentByIdIfExist(id);
        comment.setContent(commentDto.getContent());
        comment.setGood(commentDto.getGood());
        Comment commentUpdated = commentRepository.save(comment);
        return commentMapper.mapEntityToDto(commentUpdated);
    }

    public void toggleLikeComment(long id, boolean isLikeSet){
        Comment comment = getCommentByIdIfExist(id);
        int diff = isLikeSet ? 1 : -1;
        comment.setGood(comment.getGood() + diff);
        commentRepository.save(comment);
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
