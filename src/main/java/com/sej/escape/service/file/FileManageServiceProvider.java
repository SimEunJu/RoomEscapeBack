package com.sej.escape.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileManageServiceProvider {

    private final LocalFileManageService local;
    private final S3FileManageService s3;

    public FileManageService get(FileManage fileManage){
        switch (fileManage){
            case S3: return s3;
            case LOCAL: return local;
            default: throw new UnsupportedOperationException();
        }
    }
}
