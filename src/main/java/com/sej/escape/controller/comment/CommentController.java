package com.sej.escape.controller.comment;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.sej.escape.dto.comment.*;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.error.exception.validation.InvalidRequestParamter;
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
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final StoreCommentService storeCommentService;
    private final BoardCommentService boardCommentService;

    private final long EMPTY_RAND_ID = 0;

    @GetMapping("")
    public ResponseEntity<CommentListResDto> getList(@Valid CommentReqDto reqDto){
        CommentListResDto commentList = commentService.getCommentList(reqDto);
        commentList.setType(reqDto.getType());
        return ResponseEntity.ok(commentList);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentDto> getList(@PathVariable long id){
        CommentDto commentDto = commentService.getComment(id);
        return ResponseEntity.ok(commentDto);
    }

    @PostMapping("/new")
    public ResponseEntity<CommentResDto> addComment(@RequestBody CommentModifyReqDto commentModifyReqDto){
        String type = commentModifyReqDto.getAncestor().getType();
        Function<CommentModifyReqDto, CommentResDto> addFunc = null;

        switch (type){
            case "store":
                addFunc = (CommentModifyReqDto reqDto) -> storeCommentService.addComment(reqDto);
                break;
            case "board":
                addFunc = (CommentModifyReqDto reqDto) -> boardCommentService.addComment(reqDto);
        }

        CommentResDto commentResDto = commentService.addComment(commentModifyReqDto, addFunc);
        commentResDto.setRandId(commentModifyReqDto.getRandId());
        commentResDto.setType("add");
        commentResDto.setAncestorType(type);

        return ResponseEntity.ok(commentResDto);
    }

    private CommentResDto getResDtoWithRandId(String type, long id, long randId){
        CommentResDto resDto = getResDto(type, id);
        resDto.setRandId(randId);
        return resDto;
    }

    private CommentResDto getResDto(String type, long id){
        return CommentResDto.resBuilder()
                .type(type)
                .id(id)
                .randId(EMPTY_RAND_ID)
                .hasError(false)
                .build();
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<CommentResDto> deleteComment(@PathVariable long id, @RequestBody Map<String, Object> paramMap){
        String type = (String) paramMap.get("type");
        if(type == null) throw new InvalidRequestParamter(String.format("[type = %s] 값이 올바르지 않습니다.", type));

        long deleteId = commentService.deleteComment(id);
        CommentResDto resDto = getResDto("delete", deleteId);
        resDto.setAncestorType(type);
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CommentResDto> updateComment(@PathVariable long id, @RequestBody CommentModifyReqDto modifyReqDto){
        CommentResDto resDto =  commentService.updateComment(id, modifyReqDto);
        resDto.setAncestorType(modifyReqDto.getAncestor().getType());
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/hide/{id}")
    public ResponseEntity<CommentResDto> toggleHideComment(@PathVariable long id, @RequestBody Map<String, Object> paramMap){
        Boolean isHidden = (Boolean) paramMap.get("isHidden");
        String type = (String) paramMap.get("type");
        if(isHidden == null || type == null) throw new InvalidRequestParamter(String.format("[isHidden = %b, type = %s] 값이 올바르지 않습니다.", isHidden, type));

        CommentResDto resDto = commentService.toggleHideComment(id, isHidden);
        resDto.setType("hide");
        resDto.setAncestorType(type);
        return ResponseEntity.ok(resDto);
    }
}
