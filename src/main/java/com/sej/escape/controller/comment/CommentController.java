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

    private final long EMPTY_RAND_ID = 0;

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

        CommentResDto commentResDto = getResDto("add", id, commentReqDto.getRandId());

        return ResponseEntity.ok(commentResDto);
    }

    private CommentResDto getResDto(String type, long id, long randId){
        return CommentResDto.builder()
                .type(type)
                .id(id)
                .randId(randId)
                .hasError(false)
                .build();
    }

    @PatchMapping("/report/{id}")
    public ResponseEntity<CommentResDto> reportComment(@PathVariable long id){
        long reportId = commentService.reportComment(id);
        CommentResDto resDto = getResDto("report", reportId, EMPTY_RAND_ID);
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<CommentResDto> deleteComment(@PathVariable long id){
        long deleteId = commentService.deleteComment(id);
        CommentResDto resDto = getResDto("delete", deleteId, EMPTY_RAND_ID);
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long id, CommentDto commentDto){
        CommentDto commentDtoUpdated =  commentService.updateComment(id, commentDto);
        return ResponseEntity.ok(commentDtoUpdated);
    }

    @PatchMapping("/like/{id}")
    public ResponseEntity<CommentResDto> toggleLikeComment(@PathVariable long id, boolean isGood){
        long likeId = commentService.toggleLikeComment(id, isGood);
        CommentResDto resDto = getResDto("toggle", likeId, EMPTY_RAND_ID);
        return ResponseEntity.ok(resDto);
    }


}
