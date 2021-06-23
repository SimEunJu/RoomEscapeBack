package com.sej.escape.controller.comment;

import com.sej.escape.service.comment.BoardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/board")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

}
