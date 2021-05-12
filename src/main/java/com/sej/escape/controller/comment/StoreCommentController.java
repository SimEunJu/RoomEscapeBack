package com.sej.escape.controller.comment;


import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.service.comment.StoreCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/store")
public class StoreCommentController {

    private final StoreCommentService storeCommentService;

    @GetMapping("/member")
    public ResponseEntity<CommentListResDto> getCommentsByMember(CommentListReqDto reqDto){
        CommentListResDto comments = storeCommentService.getCommentsByMember(reqDto);
        comments.setAncestor(reqDto.getAncestor());
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/new")
    public ResponseEntity<StoreCommentDto> addComment(@RequestBody CommentModifyReqDto reqDto){
        StoreCommentDto comment = storeCommentService.addCommentAndRetDetail(reqDto);
        comment.setAncestorType("store");
        comment.setRandId(reqDto.getRandId());
        return ResponseEntity.ok(comment);
    }
}
