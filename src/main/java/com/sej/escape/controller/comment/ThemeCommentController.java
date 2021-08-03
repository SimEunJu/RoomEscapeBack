package com.sej.escape.controller.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.comment.theme.ThemeCommentDetailDto;
import com.sej.escape.dto.comment.theme.ThemeCommentDto;
import com.sej.escape.dto.comment.theme.ThemeCommentForListByMemberDto;
import com.sej.escape.dto.comment.theme.ThemeCommentResDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.service.comment.ThemeCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/theme")
@Api("테마 후기")
public class ThemeCommentController {

    private final ThemeCommentService themeCommentService;

    @ApiOperation("테마 후기 등록")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ThemeCommentResDto> addComment(@RequestBody ThemeCommentDto reqDto){
        ThemeCommentResDto commentDto = themeCommentService.addComment(reqDto);

        commentDto.setRandId(commentDto.getRandId());

        return ResponseEntity.ok(commentDto);
    }

    @ApiOperation("테마 후기 리스트")
    @GetMapping
    public ResponseEntity<CommentListResDto> getComments(@Valid CommentReqDto reqDto){
        CommentListResDto commentList = themeCommentService.getCommentList(reqDto);

        commentList.setAncestor(Ancestor.builder().type("theme").build());
        commentList.setHasRecomment(false);

        return ResponseEntity.ok(commentList);
    }

    @ApiOperation("최근/인기 테마 후기 리스트")
    @GetMapping("/by/{type}")
    public ResponseEntity<Map<String, Object>> getCommentsByType(@ApiParam("최근/인기") @PathVariable String type){
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

    @ApiOperation("테마 후기 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ThemeCommentDetailDto> getComment(@ApiParam("아이디") @PathVariable long id){
        ThemeCommentDetailDto dto = themeCommentService.getComment(id);
        return ResponseEntity.ok(dto);
    }

    @ApiOperation("로그인한 사용자가 작성한 테마 후기 리스트")
    @GetMapping("/member")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentListResDto> getCommentsByMember(CommentListReqDto reqDto){
        CommentListResDto comments = themeCommentService.getCommentsByMember(reqDto);
        comments.setAncestor(reqDto.getAncestor());
        return ResponseEntity.ok(comments);
    }

    @ApiOperation("테마 후기 수정")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ThemeCommentResDto> updateComment(@ApiParam("아이디") @PathVariable long id, @RequestBody ThemeCommentDto modifyReqDto){
        ThemeCommentResDto resDto =  themeCommentService.updateComment(id, modifyReqDto);
        resDto.setType("theme");
        return ResponseEntity.ok(resDto);
    }

    @ApiOperation("테마 후기 수정")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentResDto> deleteComment(@ApiParam("아이디") @PathVariable long id){
        long deleteId = themeCommentService.deleteComment(id);
        CommentResDto resDto = CommentResDto.resBuilder().id(deleteId).type("delete").build();
        return ResponseEntity.ok(resDto);
    }

    @ApiOperation("테마 후기 숨김")
    @PatchMapping("/hide/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ThemeCommentResDto> toggleHideComment(@ApiParam("아이디") @PathVariable long id, @RequestBody @Valid CommentModifyReqDto modifyReqDto){
        ThemeCommentResDto resDto = themeCommentService.toggleHideComment(id, modifyReqDto.isHidden());
        resDto.setAncestor(modifyReqDto.getAncestor());
        resDto.setType("hide");
        return ResponseEntity.ok(resDto);
    }
}
