package com.sej.escape.dto.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class FileDeleteByUrlDto {

    @ApiModelProperty("이미지 url")
    @NonNull private String url;
}
