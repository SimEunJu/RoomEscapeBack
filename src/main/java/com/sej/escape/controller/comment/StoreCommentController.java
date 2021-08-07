package com.sej.escape.controller.comment;


import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.comment.store.StoreCommentDto;
import com.sej.escape.service.comment.StoreCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/store")
@PreAuthorize("hasRole('USER')")
@Api("가게 댓글")
public class StoreCommentController {

    private final StoreCommentService storeCommentService;

    @ApiOperation("로그인한 사용자가 작성한 가게 댓글 리스트")
    @GetMapping("/member")
    public ResponseEntity<CommentListResDto> getCommentsByMember(CommentListReqDto reqDto){
        CommentListResDto comments = storeCommentService.getCommentsByMember(reqDto);

        comments.setAncestor(reqDto.getAncestor());

        return ResponseEntity.ok(comments);
    }

    @ApiOperation("가게 댓글 등록")
    @PostMapping
    public ResponseEntity<StoreCommentDto> addComment(@RequestBody CommentModifyReqDto reqDto){
        StoreCommentDto comment = storeCommentService.addCommentAndRetDetail(reqDto);

        comment.setAncestor(reqDto.getAncestor());
        comment.setRandId(reqDto.getRandId());

        return ResponseEntity.ok(comment);
    }
}
