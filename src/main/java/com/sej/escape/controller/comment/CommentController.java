package com.sej.escape.controller.comment;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.sej.escape.dto.comment.*;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.error.exception.validation.InvalidRequestParamter;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.service.comment.BoardCommentService;
import com.sej.escape.service.comment.CommentService;
import com.sej.escape.service.comment.StoreCommentService;
import com.sej.escape.service.comment.ThemeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final StoreCommentService storeCommentService;
    private final BoardCommentService boardCommentService;

    private final long EMPTY_RAND_ID = 0;

    @GetMapping
    public ResponseEntity<CommentListResDto> getComments(@Valid CommentReqDto reqDto){
        CommentListResDto commentList = commentService.getCommentList(reqDto);
        commentList.setAncestor(reqDto.getType().getAncestor());
        commentList.setHasRecomment(reqDto.getType().hasRecomment());
        return ResponseEntity.ok(commentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable long id){
        CommentDto commentDto = commentService.getComment(id);
        return ResponseEntity.ok(commentDto);
    }

    @PostMapping
    public ResponseEntity<CommentResDto> addComment(@RequestBody @Valid CommentModifyReqDto commentModifyReqDto){
        String type = commentModifyReqDto.getAncestor().getType();
        Function<CommentModifyReqDto, CommentResDto> addFunc = null;

        switch (type){
            case "store":
                addFunc = (CommentModifyReqDto reqDto) -> storeCommentService.addComment(reqDto);
                break;
            case "board":
                addFunc = (CommentModifyReqDto reqDto) -> boardCommentService.addComment(reqDto);
                break;
            default: throw new UnDefinedConstantException(String.format("[%s] 정의되지 않은 comment type입니다.", type));
        }

        CommentResDto commentResDto = commentService.addComment(commentModifyReqDto, addFunc);
        commentResDto.setRandId(commentModifyReqDto.getRandId());
        commentResDto.setType("add");
        commentResDto.setAncestor(commentModifyReqDto.getAncestor());

        return ResponseEntity.ok(commentResDto);
    }

    private CommentResDto getResDto(String type, long id){
        return CommentResDto.resBuilder()
                .type(type)
                .id(id)
                .randId(EMPTY_RAND_ID)
                .hasError(false)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResDto> deleteComment(@PathVariable long id, @RequestBody @Valid CommentModifyReqDto commentModifyReqDto){
        long deleteId = commentService.deleteComment(id);
        CommentResDto resDto = getResDto("delete", deleteId);
        resDto.setType("delete");
        resDto.setAncestor(commentModifyReqDto.getAncestor());
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentResDto> updateComment(@PathVariable long id, @RequestBody @Valid CommentModifyReqDto modifyReqDto){
        CommentResDto resDto =  commentService.updateComment(id, modifyReqDto);
        resDto.setAncestor(modifyReqDto.getAncestor());
        resDto.setType("update");
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/hide/{id}")
    public ResponseEntity<CommentResDto> toggleHideComment(@PathVariable long id, @RequestBody @Valid CommentModifyReqDto modifyReqDto){

        CommentResDto resDto = commentService.toggleHideComment(id, modifyReqDto.isHidden());
        resDto.setType("hide");
        resDto.setAncestor(modifyReqDto.getAncestor());
        return ResponseEntity.ok(resDto);
    }
}
