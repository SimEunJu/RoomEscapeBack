package com.sej.escape.controller.file;

import com.sej.escape.dto.file.FileReqDto;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@UtilityClass
public class FileControllerUtils {

    // TODO: 중복 제거
    public FileReqDto getFileReqDto(FileReqDto reqDto){
        MultipartFile uploadFile = reqDto.getUploadFile();
        String originalName = uploadFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);

        int contentTypeIdx = originalName.lastIndexOf(".");
        String contentType = originalName.substring(contentTypeIdx+1, fileName.length());

        String subPath = getSubPathByCurDate();
        makeDirs(subPath);

        String name = getRandomName();

        reqDto.setOriginalName(fileName);
        reqDto.setName(name+"."+contentType);
        reqDto.setSubPath(subPath);

        return reqDto;
    }

    public FileReqDto getFileReqDto(MultipartFile uploadFile){
        String originalName = uploadFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);

        int contentTypeIdx = originalName.lastIndexOf(".");
        String contentType = originalName.substring(contentTypeIdx+1, fileName.length());

        String subPath = getSubPathByCurDate();
        makeDirs(subPath);

        String name = getRandomName();

        FileReqDto fileReqDto = FileReqDto.builder()
                .originalName(fileName)
                .name(name+"."+contentType)
                .subPath(subPath)
                .uploadFile(uploadFile)
                .build();

        return fileReqDto;
    }

    private String getSubPathByCurDate(){
        String dateYmd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return dateYmd;
    }

    private void makeDirs(String path){
        File uploadPath = new File(path);
        if(uploadPath.exists() == false){
            uploadPath.mkdirs();
        }
    }

    private String getRandomName(){
        String uuid = UUID.randomUUID().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H-m-s-N");
        String time = LocalTime.now().format(formatter).toString().substring(0, 12);
        return time+"_"+uuid;
    }
}
