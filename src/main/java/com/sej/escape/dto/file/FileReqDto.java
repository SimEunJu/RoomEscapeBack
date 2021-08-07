package com.sej.escape.dto.file;

import com.sej.escape.constants.dto.FileType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileReqDto {

    //private String name;
    //private String originalName;
    //private String subPath;
    //private String contentType;
    //private String rootPath;
    //private int seq;

    //private long id;
    //private long referId;

    @ApiModelProperty("파일") private MultipartFile uploadFile;
    @ApiModelProperty("파일 업로드 시 사용되는 랜덤 아이디") private long randomId;

    @ApiModelProperty("")
    @NonNull private FileType type;


}
