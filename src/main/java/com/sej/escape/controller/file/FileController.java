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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Log4j2
@PreAuthorize("hasRole('USER')")
public class FileController {

    private final FileManageServiceProvider fileManagerServiceProvider;
    private final FileService fileService;

    @DeleteMapping("/cloud")
    public ResponseEntity<Void> deleteFiles(@RequestBody List<Long> ids){
        fileService.deleteFiles(ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cloud/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable long id){
        int delCnt = fileService.deleteFile(id);
        if(delCnt != 1) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/cloud", consumes = { "multipart/form-data" })
    public ResponseEntity<FileResDto> uploadFileToCloud(@Valid FileReqDto reqDto) throws FileUploadException {

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
