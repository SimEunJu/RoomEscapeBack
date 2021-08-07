package com.sej.escape.service.file.manage;

import com.sej.escape.dto.file.FileDto;
import com.sej.escape.dto.file.FileReqDto;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public interface FileManageService {

    FileDto uploadFile(FileDto fileDto) throws FileUploadException;

}
