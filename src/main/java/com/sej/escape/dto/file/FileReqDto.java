package com.sej.escape.dto.file;

import com.sej.escape.constants.dto.FileType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileReqDto {

    private String name;
    private String originalName;
    private String rootPath;
    private String subPath;
    private MultipartFile uploadFile;
    private int seq;
    private String contentType;

    private long randomId;
    private long id;
    private long referId;
    @NonNull private FileType type;

    public String getUrl(){
        return rootPath + "/" + subPath + "/" + name;
    }
}
