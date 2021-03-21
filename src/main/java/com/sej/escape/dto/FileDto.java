package com.sej.escape.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileDto {

    private String name;
    private String originalName;
    private String rootPath;
    private String subPath;

    public String getUrl(){
        return rootPath+"/"+subPath+"/"+name;
    }

}
