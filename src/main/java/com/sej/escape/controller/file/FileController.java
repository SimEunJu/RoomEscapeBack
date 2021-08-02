package com.sej.escape.controller.file;

import com.sej.escape.dto.file.FileDto;
import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.service.file.*;
import com.sej.escape.service.file.manage.FileManage;
import com.sej.escape.service.file.manage.FileManageService;
import com.sej.escape.service.file.manage.FileManageServiceProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@PreAuthorize("hasRole('USER')")
@Api("파일")
public class FileController {

    private final FileManageServiceProvider fileManagerServiceProvider;
    private final FileService fileService;

    @ApiOperation("파일 리스트 삭제")
    @DeleteMapping("/cloud")
    public ResponseEntity<Void> deleteFiles(@RequestBody List<Long> ids){
        fileService.deleteFiles(ids);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("파일 삭제")
    @DeleteMapping("/cloud/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable long id){
        fileService.deleteFile(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("파일 업로드")
    @PostMapping(value="/cloud", consumes = { "multipart/form-data" })
    public ResponseEntity<FileResDto> uploadFileToCloud(@Valid FileReqDto reqDto) throws FileUploadException {

        FileDto fileDto = FileControllerUtils.getFileReqDto(reqDto);

        FileManageService fileManageService = fileManagerServiceProvider.get(FileManage.CLOUD);

        FileResDto fileResDto = fileService.saveFile(fileDto, fileManageService);
        fileResDto.setRandomId(reqDto.getRandomId());

        return ResponseEntity.ok(fileResDto);
    }

    @ApiOperation("로컬에 파일 업로드")
    @PostMapping("/local")
    public ResponseEntity<FileResDto> uploadFileToLocal(@RequestBody @Valid FileReqDto reqDto) throws FileUploadException {

        FileDto fileDto = FileControllerUtils.getFileReqDto(reqDto);

        FileManageService fileManageService = fileManagerServiceProvider.get(FileManage.LOCAL);

        FileResDto fileResDto = fileService.saveFile(fileDto, fileManageService);
        fileResDto.setRandomId(reqDto.getRandomId());

        return ResponseEntity.ok(fileResDto);
    }
}
