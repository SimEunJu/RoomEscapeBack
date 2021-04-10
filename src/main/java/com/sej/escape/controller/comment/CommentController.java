package com.sej.escape.controller.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.dto.comment.CommentResDto;
import com.sej.escape.entity.comment.BoardComment;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.error.ErrorRes;
import com.sej.escape.service.comment.BoardCommentService;
import com.sej.escape.service.comment.CommentService;
import com.sej.escape.service.comment.StoreCommentService;
import com.sun.mail.iap.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.ToLongFunction;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final StoreCommentService storeCommentService;
    private final BoardCommentService boardCommentService;

    @PostMapping("/new")
    public ResponseEntity<CommentResDto> addComment(@RequestBody CommentReqDto commentReqDto){
        String type = commentReqDto.getAncestor().getType();
        ToLongFunction<CommentReqDto> addFunc = null;

        switch (type){
            case "store":
                addFunc = (CommentReqDto reqDto) -> storeCommentService.addComment(reqDto);
                break;
            case "board":
                addFunc = (CommentReqDto reqDto) -> boardCommentService.addComment(reqDto);
        }

        long id = commentService.addComment(commentReqDto, addFunc);

        CommentResDto commentResDto = getResDto("add", id, commentReqDto, null);

        return ResponseEntity.ok(commentResDto);
    }

    private CommentResDto getResDto(String type, long id, CommentReqDto reqDto, ErrorRes err){
        return CommentResDto.builder()
                .type(type)
                .id(id)
                .randId(reqDto.getRandId())
                .hasError(err != null)
                .error(err)
                .build();
    }

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
