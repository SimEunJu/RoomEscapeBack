package com.sej.escape.controller.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.theme.ThemeForListDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.service.comment.ThemeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/theme")
public class ThemeCommentController {

    private final ThemeCommentService themeCommentService;

    @PostMapping("/new")
    public ResponseEntity<ThemeCommentResDto> addComment(@RequestBody ThemeCommentDto reqDto){
        ThemeCommentResDto commentDto = themeCommentService.addComment(reqDto);
        commentDto.setRandId(commentDto.getRandId());
        return ResponseEntity.ok(commentDto);
    }

    @GetMapping
    public ResponseEntity<List<ThemeForListDto>> getComments(){
        List<ThemeForListDto> CommentDto = new ArrayList<>();
        return ResponseEntity.ok(CommentDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeCommentDto> getComment(@PathVariable long id){
        ThemeCommentDto dto = themeCommentService.getComment(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/member")
    public ResponseEntity<CommentListResDto> getCommentsByMember(CommentListReqDto reqDto){
        CommentListResDto comments = themeCommentService.getCommentsByMember(reqDto);
        comments.setType("theme");
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

    @PatchMapping("/update/{id}")
    public ResponseEntity<ThemeCommentResDto> updateComment(@PathVariable long id, @RequestBody ThemeCommentDto modifyReqDto){
        ThemeCommentResDto resDto =  themeCommentService.updateComment(id, modifyReqDto);
        return ResponseEntity.ok(resDto);
    }

}
