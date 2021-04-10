package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.repository.comment.CommentRepository;
import com.sej.escape.repository.comment.StoreCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreCommentService {

    private final StoreCommentRepository stotreCommentRepository;
    private final CommentMapper commentMapper;

    public long addComment(CommentReqDto commentReqDto){
        StoreComment storeComment = commentMapper.mapReqDtoToStoreComment(commentReqDto);
        stotreCommentRepository.save(storeComment);
        return storeComment.getId();
    }
}
