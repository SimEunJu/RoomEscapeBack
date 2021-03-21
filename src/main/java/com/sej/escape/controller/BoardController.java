package com.sej.escape.controller;

import com.sej.escape.dto.ModifyResDto;
import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@AllArgsConstructor
public class BoardController {

    private BoardService boardService;

    @GetMapping
    public ResponseEntity<PageResDto> getBoards(PageReqDto pageReqDto){
        PageResDto pageResDto = boardService.getBoards(pageReqDto);
        return ResponseEntity.ok(pageResDto);
    }

    @GetMapping("/:id")
    public ResponseEntity<BoardDto> getBoard(@PathVariable long id){
        BoardDto boardDto = boardService.getBoard(id);
        return ResponseEntity.ok(boardDto);
    }

    @PatchMapping("/delete")
    public ResponseEntity<ModifyResDto> deleteBoards(List<Long> ids){
        long deleteCnt = boardService.deleteBoards(ids);
        long reqCnt = ids.size();
        return ResponseEntity.ok(new ModifyResDto(reqCnt, deleteCnt, reqCnt == deleteCnt));
    }

    @PatchMapping("/update/:id")
    public ResponseEntity<BoardDto> updateBoard(BoardDto boardDto){
        BoardDto boardDtoUp = boardService.updateBoard(boardDto);
        return ResponseEntity.ok(boardDtoUp);
    }

    @PostMapping("/new")
    public void addBoard(BoardDto boardDto){

    }

}
