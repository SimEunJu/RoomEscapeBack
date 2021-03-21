package com.sej.escape.service.file;

import com.sej.escape.dto.FileReqDto;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

public interface FileManageService {

    FileReqDto uploadFile(FileReqDto fileReqDto) throws FileUploadException;

}
