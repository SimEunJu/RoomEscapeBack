package com.sej.escape.controller.comment;


import com.sej.escape.dto.comment.CommentListReqDto;
import com.sej.escape.dto.comment.CommentListResDto;
import com.sej.escape.dto.comment.CommentResDto;
import com.sej.escape.dto.comment.StoreCommentDto;
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
        CommentListResDto comments = storeCommentService.getComments(reqDto);
        return ResponseEntity.ok(comments);
    }
}
