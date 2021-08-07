package com.sej.escape.controller.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.service.comment.BoardCommentService;
import com.sej.escape.service.comment.CommentService;
import com.sej.escape.service.comment.StoreCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Api("댓글")
public class CommentController {

    private final CommentService commentService;
    private final StoreCommentService storeCommentService;
    private final BoardCommentService boardCommentService;

    private final long EMPTY_RAND_ID = 0;

    @ApiOperation("댓글 리스트")
    @GetMapping
    public ResponseEntity<CommentListResDto> getComments(@Valid CommentReqDto reqDto){
        CommentListResDto commentList = commentService.getCommentList(reqDto);

        commentList.setAncestor(reqDto.getType().getAncestor());
        commentList.setHasRecomment(reqDto.getType().hasRecomment());

        return ResponseEntity.ok(commentList);
    }

    @ApiOperation("댓글 조회")
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComment(@ApiParam("댓글 아이디") @PathVariable long id){
        CommentDto commentDto = commentService.getComment(id);
        return ResponseEntity.ok(commentDto);
    }

    @ApiOperation("댓글 추가")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
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
        commentResDto.setActionType("add");
        commentResDto.setAncestor(commentModifyReqDto.getAncestor());

        return ResponseEntity.ok(commentResDto);
    }

    private boolean hasRecomment(String type){
        if("board".equals(type)) return true;
        return false;
    }

    @ApiOperation("댓글 삭제")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentResDto> deleteComment(@ApiParam("댓글 아이디") @PathVariable long id,
                                                       @RequestBody @Valid CommentModifyReqDto reqDto){
        commentService.deleteComment(id);

        CommentResDto resDto = CommentResDto.resBuilder()
                .id(id)
                .actionType("delete")
                .hasRecomment(hasRecomment(reqDto.getAncestor().getType()))
                .ancestor(reqDto.getAncestor())
                .build();

        return ResponseEntity.ok(resDto);
    }

    @ApiOperation("댓글 수정")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentResDto> updateComment(@ApiParam("댓글 아이디") @PathVariable long id,
                                                       @RequestBody @Valid CommentModifyReqDto modifyReqDto){
        CommentResDto resDto = commentService.updateComment(id, modifyReqDto);

        resDto.setAncestor(modifyReqDto.getAncestor());
        resDto.setActionType("update");

        return ResponseEntity.ok(resDto);
    }

    @ApiOperation("숨김 여부 변경")
    @PatchMapping("/hide/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentResDto> toggleHideComment(@ApiParam("댓글 아이디") @PathVariable long id,
                                                           @RequestBody @Valid CommentModifyReqDto modifyReqDto){

        CommentResDto resDto = commentService.toggleHideComment(id, modifyReqDto.isHidden());

        resDto.setActionType("hide");
        resDto.setAncestor(modifyReqDto.getAncestor());

        return ResponseEntity.ok(resDto);
    }
}
