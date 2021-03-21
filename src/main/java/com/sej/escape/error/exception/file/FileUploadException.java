package com.sej.escape.error.exception.file;

import com.sej.escape.error.ErrorCode;
import com.sej.escape.error.exception.BusinessException;

public class FileUploadException extends BusinessException {

    public FileUploadException(String message) {
        super(message, ErrorCode.FILE_UPLOAD_FAIL);
    }

    public FileUploadException(String message, Exception e) {
        super(message, ErrorCode.FILE_UPLOAD_FAIL);
        super.initCause(e);
    }
}
