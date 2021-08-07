package com.sej.escape.controller;

import com.sej.escape.constants.dto.BoardType;
import com.sej.escape.dto.board.*;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.service.board.BoardService;
import com.sej.escape.service.board.IBoardService;
import com.sej.escape.service.board.NoticeBoardService;
import com.sej.escape.service.board.ReqBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Api("게시판")
public class BoardController {

    private final BoardService boardService;
    private final NoticeBoardService noticeBoardService;
    private final ReqBoardService reqBoardService;

    private IBoardService getServiceByType(BoardType boardType){
        switch (boardType){
            case NOTICE: return noticeBoardService;
            case REQ: return reqBoardService;
            default: throw new UnDefinedConstantException(String.format("%s는 정의되지 않은 게시판 타입입니다.", boardType.toString()));
        }
    }

    @ApiOperation("게시판 리스트")
    @GetMapping
    public ResponseEntity<PageResDto> getBoards(@Valid BoardReqDto pageReqDto){
        PageResDto pageResDto = boardService.getBoards(pageReqDto, getServiceByType(pageReqDto.getType()));
        pageResDto.setType(pageReqDto.getType().getTypeString());
        return ResponseEntity.ok(pageResDto);
    }

    @ApiOperation("게시글 조회")
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoard(@ApiParam("게시글 아이디") @PathVariable long id){
        BoardDto boardDto = boardService.getBoard(id);
        return ResponseEntity.ok(boardDto);
    }

    @ApiOperation("게시글 삭제")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BoardDeleteResDto> deleteBoard(@ApiParam("게시글 아이디") @PathVariable long id, @RequestBody BoardModifyReqDto reqDto){
        boardService.deleteBoard(id);
        BoardDeleteResDto resDto = BoardDeleteResDto.builder()
                .id(id)
                .type(reqDto.getType().getTypeString())
                .build();
        return ResponseEntity.ok(resDto);
    }

    @ApiOperation("게시글 리스트 삭제")
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BoardDeleteResDto> deleteBoards(@RequestBody BoardModifyReqDto reqDto){
        boardService.deleteBoards(reqDto.getIds());
        BoardDeleteResDto resDto = BoardDeleteResDto.builder()
                .ids(reqDto.getIds())
                .type(reqDto.getType().getTypeString())
                .build();
        return ResponseEntity.ok(resDto);
    }

    @ApiOperation("게시글 수정")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BoardDto> updateBoard(@RequestBody BoardDto boardDto){
        BoardDto boardDtoUp = boardService.updateBoard(boardDto);
        return ResponseEntity.ok(boardDtoUp);
    }

    @ApiOperation("게시글 추가")
    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BoardResDto> addBoard(@RequestBody BoardDto boardDto) throws FileUploadException {
        BoardResDto boardResDto = boardService.addBoard(boardDto, getServiceByType(boardDto.getType()));
        return ResponseEntity.ok(boardResDto);
    }

}
