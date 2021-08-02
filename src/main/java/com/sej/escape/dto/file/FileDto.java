package com.sej.escape.dto.file;

import com.sej.escape.constants.dto.FileType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileDto {
    private String name;
    private String originalName;
    private String contentType;
    private String subPath;
    private String rootPath;

    private long id;
    private long referId;
    private int seq;

    private MultipartFile uploadFile;
    private FileType type;

    public String getUrl(){
        return rootPath + "/" + subPath + "/" + name;
    }
}
