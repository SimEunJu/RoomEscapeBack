package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.entity.comment.BoardComment;
import com.sej.escape.repository.comment.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardCommentRepository boardCommentRepository;
    private final CommentMapper commentMapper;

    public long addComment(CommentReqDto commentReqDto){
        BoardComment boardComment = commentMapper.mapReqDtoToBoardComment(commentReqDto);
        boardCommentRepository.save(boardComment);
        return boardComment.getId();
    }
}
