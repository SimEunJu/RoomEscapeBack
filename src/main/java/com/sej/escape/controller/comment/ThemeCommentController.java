package com.sej.escape.controller.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentListReqDto;
import com.sej.escape.dto.comment.CommentListResDto;
import com.sej.escape.dto.theme.ThemeForListDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.service.comment.ThemeCommentService;
import lombok.RequiredArgsConstructor;
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

    private final ThemeCommentService themeCommentService;

    @GetMapping
    public ResponseEntity<List<ThemeForListDto>> getComments(){
        List<ThemeForListDto> CommentDto = new ArrayList<>();
        return ResponseEntity.ok(CommentDto);
    }

    @GetMapping("/member")
    public ResponseEntity<CommentListResDto> getCommentsByMember(CommentListReqDto reqDto){
        CommentListResDto comments = themeCommentService.getCommentsByMember(reqDto);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/by/{type}")
    public ResponseEntity<Map<String, Object>> getCommentsByType(@PathVariable String type){
        List<CommentDto> commentDtos = null;
        PageReqDto pageReqDto = new PageReqDto();
        switch (type){
            case "latest":
                commentDtos = themeCommentService.readLatestComments(pageReqDto);
                break;
            case "top":
                commentDtos = themeCommentService.readTopComments(pageReqDto);
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
