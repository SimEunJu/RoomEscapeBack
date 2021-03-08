package com.sej.escape.controller.comment;

import com.sej.escape.dto.CommentDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.service.CommentService;
import com.sej.escape.service.ThemeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/theme")
public class ThemeCommentController {

    private CommentService commentService;

    @GetMapping("/top")
    public ResponseEntity<List<CommentDto>> getTopReviews(PageReqDto pageReqDto){
        List<CommentDto> comments = commentService.readTopComments(pageReqDto);
        return ResponseEntity.ok(comments);
    }

}
