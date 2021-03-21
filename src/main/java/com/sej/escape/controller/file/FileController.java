package com.sej.escape.controller.file;

import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.service.file.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file/upload")
@RequiredArgsConstructor
@Log4j2
public class FileController {

    private final FileManageServiceProvider fileManagerServiceProvider;
    private final FileService fileService;

    @PostMapping("/cloud")
    public ResponseEntity<FileResDto> uploadFileToCloud(@RequestPart MultipartFile uploadFile) throws FileUploadException {

        FileReqDto fileReqDto = FileControllerUtils.getFileReqDto(uploadFile);
        FileManageService fileManageService = fileManagerServiceProvider.get(FileManage.S3);
        FileResDto fileResDto = fileService.saveFile(fileReqDto, fileManageService);

        return ResponseEntity.ok(fileResDto);
    }

    @PostMapping("/local")
    public ResponseEntity<FileResDto> uploadFileToLocal(@RequestPart MultipartFile uploadFile) throws FileUploadException {

        FileReqDto fileReqDto = FileControllerUtils.getFileReqDto(uploadFile);
        FileManageService fileManageService = fileManagerServiceProvider.get(FileManage.LOCAL);
        FileResDto fileResDto = fileService.saveFile(fileReqDto, fileManageService);

        return ResponseEntity.ok(fileResDto);
    }
}
