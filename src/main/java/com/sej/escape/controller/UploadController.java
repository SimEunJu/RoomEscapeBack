package com.sej.escape.controller;

import com.sej.escape.dto.FileReqDto;
import com.sej.escape.dto.FileResDto;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/file/upload")
@RequiredArgsConstructor
@Log4j2
public class UploadController {

    private final FileManageServiceProvider fileManagerServiceProvider;
    private final FileService fileService;

    @PostMapping("/cloud")
    public ResponseEntity<FileResDto> uploadFileToCloud(@RequestPart MultipartFile uploadFile) throws FileUploadException {

        FileReqDto fileReqDto = getFileReqDto(uploadFile);
        FileManageService fileManageService = fileManagerServiceProvider.get(FileManage.S3);
        FileResDto fileResDto = fileService.saveFile(fileReqDto, fileManageService);

        return ResponseEntity.ok(fileResDto);
    }

    @PostMapping("/local")
    public ResponseEntity<FileResDto> uploadFileToLocal(@RequestPart MultipartFile uploadFile) throws FileUploadException {

        FileReqDto fileReqDto = getFileReqDto(uploadFile);
        FileManageService fileManageService = fileManagerServiceProvider.get(FileManage.LOCAL);
        FileResDto fileResDto = fileService.saveFile(fileReqDto, fileManageService);

        return ResponseEntity.ok(fileResDto);
    }

    private FileReqDto getFileReqDto(MultipartFile uploadFile){
        String originalName = uploadFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);

        String subPath = subPathByCurDate();
        String uuid = UUID.randomUUID().toString();
        String name = uuid + "_" + fileName;

        FileReqDto fileReqDto = FileReqDto.builder()
                .originalName(fileName)
                .name(name)
                .subPath(subPath)
                .file(uploadFile)
                .build();

        return fileReqDto;
    }

    private String subPathByCurDate(){
        String dateYmd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return dateYmd;
    }
}
