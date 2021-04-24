package com.sej.escape.controller;

import com.sej.escape.controller.file.FileControllerUtils;
import com.sej.escape.dto.ModifyResDto;
import com.sej.escape.dto.board.BoardDto;
import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.service.BoardService;
import com.sej.escape.service.file.manage.FileManageService;
import com.sej.escape.service.file.manage.FileManageServiceProvider;
import com.sej.escape.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final FileManageServiceProvider fileManageServiceProvider;

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
    public ResponseEntity<BoardDto> addBoard(BoardDto boardDto, MultipartFile multipartFile) throws FileUploadException {
        BoardDto boardResDto = boardService.addBoard(boardDto);

        FileManageService fileManageService = fileManageServiceProvider.getDefault();
        FileReqDto fileReqDto = FileControllerUtils.getFileReqDto(multipartFile);
        fileReqDto.setReferId(boardResDto.getId());
        FileResDto fileResDto = fileService.saveFile(fileReqDto, fileManageService);

        boardResDto.setFile(fileResDto);

        return ResponseEntity.ok(boardResDto);
    }

}
