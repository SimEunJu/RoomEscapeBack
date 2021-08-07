package com.sej.escape.service.file.manage;

import com.sej.escape.service.file.upload.LocalFileManageService;
import com.sej.escape.service.file.upload.S3FileManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileManageServiceProvider {

    private final LocalFileManageService local;
    private final S3FileManageService cloud;

    public FileManageService get(FileManage fileManage){
        switch (fileManage){
            case CLOUD: return cloud;
            case LOCAL: return local;
            default: throw new UnsupportedOperationException();
        }
    }

    public FileManageService getDefault(){
        return cloud;
    }
}
