package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentModifyReqDto;
import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.repository.comment.StoreCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreCommentService {

    private final StoreCommentRepository stotreCommentRepository;
    private final CommentMapper commentMapper;

    public CommentDto addComment(CommentModifyReqDto commentModifyReqDto){
        StoreComment storeComment = commentMapper.mapReqDtoToStoreComment(commentModifyReqDto);
        stotreCommentRepository.save(storeComment);
        return commentMapper.mapEntityToDto(storeComment);
    }

}
