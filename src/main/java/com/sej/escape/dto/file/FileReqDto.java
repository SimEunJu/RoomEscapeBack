package com.sej.escape.dto.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@ToString
public class FileReqDto {

    private String name;
    private String originalName;
    private String rootPath;
    private String subPath;
    private MultipartFile file;
    private int seq;

    private long boardId;

    public String getUrl(){
        return rootPath + "/" + subPath + "/" + name;
    }
}
