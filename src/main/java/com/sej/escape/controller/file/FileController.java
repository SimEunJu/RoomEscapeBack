package com.sej.escape.controller.file;

import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.service.file.*;
import com.sej.escape.service.file.manage.FileManage;
import com.sej.escape.service.file.manage.FileManageService;
import com.sej.escape.service.file.manage.FileManageServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/file/")
@RequiredArgsConstructor
@Log4j2
public class FileController {

    private final FileManageServiceProvider fileManagerServiceProvider;
    private final FileService fileService;

    @DeleteMapping("/cloud")
    public void deleteFiles(@RequestBody List<Long> ids){
        fileService.deleteFiles(ids);
    }

    @PostMapping(value="/cloud", consumes = { "multipart/form-data" })
    public ResponseEntity<FileResDto> updateFileToCloud(@Valid FileReqDto reqDto) throws FileUploadException {

        FileReqDto fileReqDto = FileControllerUtils.getFileReqDto(reqDto);
        FileManageService fileManageService = fileManagerServiceProvider.get(FileManage.S3);
        FileResDto fileResDto = fileService.saveFile(fileReqDto, fileManageService);

        return ResponseEntity.ok(fileResDto);
    }

    @PostMapping("/local")
    public ResponseEntity<FileResDto> uploadFileToLocal(@RequestBody @Valid FileReqDto reqDto) throws FileUploadException {

        FileReqDto fileReqDto = FileControllerUtils.getFileReqDto(reqDto);
        FileManageService fileManageService = fileManagerServiceProvider.get(FileManage.LOCAL);
        FileResDto fileResDto = fileService.saveFile(fileReqDto, fileManageService);

        return ResponseEntity.ok(fileResDto);
    }
}
