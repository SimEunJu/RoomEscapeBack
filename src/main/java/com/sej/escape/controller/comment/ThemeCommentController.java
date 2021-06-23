package com.sej.escape.controller.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.service.comment.ThemeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/theme")
public class ThemeCommentController {

    private final ThemeCommentService themeCommentService;

    @PostMapping
    public ResponseEntity<ThemeCommentResDto> addComment(@RequestBody ThemeCommentDto reqDto){
        ThemeCommentResDto commentDto = themeCommentService.addComment(reqDto);
        commentDto.setRandId(commentDto.getRandId());
        return ResponseEntity.ok(commentDto);
    }

    @GetMapping
    public ResponseEntity<CommentListResDto> getComments(@Valid CommentReqDto reqDto){
        CommentListResDto commentList = themeCommentService.getCommentList(reqDto);
        commentList.setAncestor(Ancestor.builder().type("theme").build());
        commentList.setHasRecomment(false);
        return ResponseEntity.ok(commentList);
    }

    @GetMapping("/by/{type}")
    public ResponseEntity<Map<String, Object>> getCommentsByType(@PathVariable String type){
        List<ThemeCommentForListByMemberDto> commentDtos = null;
        PageReqDto pageReqDto = new PageReqDto();
        switch (type){
            case "latest":
                commentDtos = themeCommentService.readLatestComments(pageReqDto);
                break;
            case "top":
                commentDtos = themeCommentService.readTopComments();
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

    @GetMapping("/{id}")
    public ResponseEntity<ThemeCommentDetailDto> getComment(@PathVariable long id){
        ThemeCommentDetailDto dto = themeCommentService.getComment(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/member")
    public ResponseEntity<CommentListResDto> getCommentsByMember(CommentListReqDto reqDto){
        CommentListResDto comments = themeCommentService.getCommentsByMember(reqDto);
        comments.setAncestor(reqDto.getAncestor());
        return ResponseEntity.ok(comments);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ThemeCommentResDto> updateComment(@PathVariable long id, @RequestBody ThemeCommentDto modifyReqDto){
        ThemeCommentResDto resDto =  themeCommentService.updateComment(id, modifyReqDto);
        resDto.setType("theme");
        return ResponseEntity.ok(resDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResDto> deleteComment(@PathVariable long id){
        long deleteId = themeCommentService.deleteComment(id);
        CommentResDto resDto = CommentResDto.resBuilder().id(deleteId).type("delete").build();
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/hide/{id}")
    public ResponseEntity<ThemeCommentResDto> toggleHideComment(@PathVariable long id, @RequestBody @Valid CommentModifyReqDto modifyReqDto){
        ThemeCommentResDto resDto = themeCommentService.toggleHideComment(id, modifyReqDto.isHidden());
        resDto.setType("hide");
        return ResponseEntity.ok(resDto);
    }
}
