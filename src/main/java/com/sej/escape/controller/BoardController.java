package com.sej.escape.controller;

import com.sej.escape.constants.dto.BoardType;
import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.dto.board.BoardModifyReqDto;
import com.sej.escape.dto.board.BoardReqDto;
import com.sej.escape.dto.board.BoardResDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.service.board.BoardService;
import com.sej.escape.service.board.IBoardService;
import com.sej.escape.service.board.NoticeBoardService;
import com.sej.escape.service.board.ReqBoardService;
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

    @GetMapping
    public ResponseEntity<PageResDto> getBoards(@Valid BoardReqDto pageReqDto){
        PageResDto pageResDto = boardService.getBoards(pageReqDto, getServiceByType(pageReqDto.getType()));
        pageResDto.setType(pageReqDto.getType().getTypeString());
        return ResponseEntity.ok(pageResDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable long id){
        BoardDto boardDto = boardService.getBoard(id);
        return ResponseEntity.ok(boardDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BoardResDto> deleteBoard(@PathVariable long id, @RequestBody BoardModifyReqDto reqDto){
        int deleteCnt = boardService.deleteBoard(id);
        BoardResDto resDto = BoardResDto.builder()
                .id(id)
                .type(reqDto.getType().getTypeString())
                .build();
        return ResponseEntity.ok(resDto);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BoardResDto> deleteBoards(@RequestBody BoardModifyReqDto reqDto){
        boardService.deleteBoards(reqDto.getIds());
        BoardResDto resDto = BoardResDto.builder()
                .ids(reqDto.getIds())
                .type(reqDto.getType().getTypeString())
                .build();
        return ResponseEntity.ok(resDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BoardDto> updateBoard(@RequestBody BoardDto boardDto){
        BoardDto boardDtoUp = boardService.updateBoard(boardDto);
        return ResponseEntity.ok(boardDtoUp);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BoardResDto> addBoard(@RequestBody BoardDto boardDto, MultipartFile multipartFile) throws FileUploadException {
        BoardResDto boardResDto = boardService.addBoard(boardDto, getServiceByType(boardDto.getType()));
        return ResponseEntity.ok(boardResDto);
    }

}
