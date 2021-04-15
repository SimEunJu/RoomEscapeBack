package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentModifyReqDto;
import com.sej.escape.entity.comment.BoardComment;
import com.sej.escape.repository.comment.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final CommentMapper commentMapper;

    public CommentDto addComment(CommentModifyReqDto commentModifyReqDto){
        BoardComment boardComment = commentMapper.mapReqDtoToBoardComment(commentModifyReqDto);
        boardCommentRepository.save(boardComment);
        return commentMapper.mapEntityToDto(boardComment);
    }
}
