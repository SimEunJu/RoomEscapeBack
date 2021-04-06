package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.repository.comment.CommentRepository;
import com.sej.escape.repository.comment.StoreCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreCommentService {

    private final CommentRepository commentRepository;
    private final StoreCommentRepository stotreCommentRepository;
    private final CommentMapper commentMapper;

    public List<CommentDto> getCommentList(CommentReqDto commentReqDto) {
        List<StoreComment>  comments = commentRepository.findAllByPaging(
                commentReqDto.getId(),
                commentReqDto.getPage(),
                commentReqDto.getSize()
        );
        return commentMapper.mapEntitesToDtos(comments);
    }
}
