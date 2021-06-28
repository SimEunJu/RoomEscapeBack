package com.sej.escape.dto.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sej.escape.constants.dto.FileType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResDto {

    private long id;
    private long randomId;
    private FileType type;
    private String originalName;
    private int seq;

    @JsonIgnore private String rootPath;
    @JsonIgnore private String subPath;
    @JsonIgnore private String name;

    public String getImgUrl() {
        return rootPath+"/"+subPath+"/"+name;
    }

    // TODO: mapping configure 설정
    public String getType(){
        if(type == null) return null;
        return type.getTypeString();
    }
}
