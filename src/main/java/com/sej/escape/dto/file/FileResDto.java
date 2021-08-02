package com.sej.escape.dto.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sej.escape.constants.dto.FileType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResDto {

    @ApiModelProperty("아이디") private long id;
    @ApiModelProperty("파일 업로드 시 사용되는 랜덤 아이디") private long randomId;

    private FileType type;

    @ApiModelProperty("원래 이름") private String originalName;
    @ApiModelProperty("순서") private int seq;

    @JsonIgnore private String rootPath;
    @JsonIgnore private String subPath;
    @JsonIgnore private String name;

    @ApiModelProperty("url")
    public String getImgUrl() {
        return rootPath+"/"+subPath+"/"+name;
    }

    @ApiModelProperty("종류")
    public String getType(){
        if(type == null) return null;
        return type.getTypeString();
    }
}
