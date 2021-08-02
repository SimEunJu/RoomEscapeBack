package com.sej.escape.controller.file;

import com.sej.escape.dto.file.FileDto;
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

    public FileDto getFileReqDto(FileReqDto reqDto){

        MultipartFile uploadFile = reqDto.getUploadFile();

        String originalName = uploadFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);

        int contentTypeIdx = originalName.lastIndexOf(".");
        String contentType = originalName.substring(contentTypeIdx+1, fileName.length());

        String subPath = getSubPathByCurDate();
        makeDirs(subPath);

        String name = getRandomName();

        FileDto fileDto = new FileDto();
        fileDto.setUploadFile(uploadFile);
        fileDto.setType(reqDto.getType());

        fileDto.setContentType(contentType);
        fileDto.setOriginalName(fileName);
        fileDto.setName(name+"."+contentType);
        fileDto.setSubPath(subPath);

        return fileDto;
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
