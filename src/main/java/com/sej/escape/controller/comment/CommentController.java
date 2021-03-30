package com.sej.escape.controller.comment;

import com.sej.escape.dto.CommentDto;
import com.sej.escape.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PatchMapping("/report/{id}")
    public void reportComment(@PathVariable long id){
        commentService.reportComment(id);
    }

    @PatchMapping("/delete/{id}")
    public void deleteComment(@PathVariable long id){
        commentService.deleteComment(id);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long id, CommentDto commentDto){
        CommentDto commentDtoUpdated =  commentService.updateComment(id, commentDto);
        return ResponseEntity.ok(commentDtoUpdated);
    }

    @PatchMapping("/like/{id}")
    public void toggleLikeComment(@PathVariable long id, boolean isGood){
        commentService.toggleLikeComment(id, isGood);
    }


}
