package com.sej.escape.controller.file;

import com.sej.escape.dto.file.FileReqDto;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@UtilityClass
public class FileControllerUtils {

    public FileReqDto getFileReqDto(MultipartFile uploadFile){
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
