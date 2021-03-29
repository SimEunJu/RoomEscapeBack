package com.sej.escape.controller.comment;

import com.sej.escape.dto.CommentDto;
import com.sej.escape.dto.ThemeDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.service.CommentService;
import com.sej.escape.service.ThemeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/theme")
public class ThemeCommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<ThemeDto>> getComments(){
        List<ThemeDto> CommentDto = new ArrayList<>();
        return ResponseEntity.ok(CommentDto);
    }

    @GetMapping("/{type}")
    public ResponseEntity<Map<String, Object>> getCommentsByType(@PathVariable String type){
        List<CommentDto> commentDtos = null;
        PageReqDto pageReqDto = new PageReqDto();
        switch (type){
            case "latest":
                commentDtos = commentService.readLatestComments(pageReqDto);
                break;
            case "top":
                commentDtos = commentService.readTopComments(pageReqDto);
                break;
            default:
                commentDtos = new ArrayList<>();
                break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("list", commentDtos);
        return ResponseEntity.ok(map);
    }

}
