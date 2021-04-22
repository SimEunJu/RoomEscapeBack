package com.sej.escape.controller.comment;

import com.sej.escape.dto.comment.CommentDto;
import com.sej.escape.dto.comment.CommentModifyReqDto;
import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.dto.comment.CommentResDto;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.service.comment.BoardCommentService;
import com.sej.escape.service.comment.CommentService;
import com.sej.escape.service.comment.StoreCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final StoreCommentService storeCommentService;
    private final BoardCommentService boardCommentService;

    private final long EMPTY_RAND_ID = 0;

    @GetMapping("")
    public ResponseEntity<List<CommentDto>> getList(@Valid CommentReqDto reqDto){
        String type = getTypeFlag( reqDto.getType() );
        reqDto.setType(type);
        List<CommentDto> commentList = commentService.getCommentList(reqDto);
        return ResponseEntity.ok(commentList);
    }

    // TODO: 추후 enum으로 매핑 후 get
    private String getTypeFlag(String type){
        return type.substring(0, 1).toUpperCase();
    }

    @PostMapping("/new")
    public ResponseEntity<CommentResDto> addComment(@RequestBody CommentModifyReqDto commentModifyReqDto){
        String type = commentModifyReqDto.getAncestor().getType();
        Function<CommentModifyReqDto, CommentResDto> addFunc = null;

        switch (type){
            case "store":
                addFunc = (CommentModifyReqDto reqDto) -> storeCommentService.addComment(reqDto);
                break;
            case "board":
                addFunc = (CommentModifyReqDto reqDto) -> boardCommentService.addComment(reqDto);
        }

        CommentResDto commentResDto = commentService.addComment(commentModifyReqDto, addFunc);
        commentResDto.setRandId(commentModifyReqDto.getRandId());
        commentResDto.setType("add");

        return ResponseEntity.ok(commentResDto);
    }

    private CommentResDto getResDtoWithRandId(String type, long id, long randId){
        CommentResDto resDto = getResDto(type, id);
        resDto.setRandId(randId);
        return resDto;
    }

    private CommentResDto getResDto(String type, long id){
        return CommentResDto.resBuilder()
                .type(type)
                .id(id)
                .randId(EMPTY_RAND_ID)
                .hasError(false)
                .build();
    }

    /*
    @PatchMapping("/report/{id}")
    public ResponseEntity<CommentResDto> reportComment(@PathVariable long id){
        long reportId = commentService.reportComment(id);
        CommentResDto resDto = getResDto("report", reportId);
        return ResponseEntity.ok(resDto);
    }
    */

    @PatchMapping("/delete/{id}")
    public ResponseEntity<CommentResDto> deleteComment(@PathVariable long id){
        long deleteId = commentService.deleteComment(id);
        CommentResDto resDto = getResDto("delete", deleteId);
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long id, @RequestBody CommentModifyReqDto modifyReqDto){
        CommentDto commentDtoUpdated =  commentService.updateComment(id, modifyReqDto);
        return ResponseEntity.ok(commentDtoUpdated);
    }

    @PatchMapping("/like/{id}")
    public ResponseEntity<CommentResDto> toggleLikeComment(@PathVariable long id, boolean isGood){
        long likeId = commentService.toggleLikeComment(id, isGood);
        CommentResDto resDto = getResDto("toggle", likeId);
        return ResponseEntity.ok(resDto);
    }


}
