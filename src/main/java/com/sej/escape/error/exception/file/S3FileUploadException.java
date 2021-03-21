package com.sej.escape.error.exception.file;

public class S3FileUploadException extends FileUploadException {

    public S3FileUploadException(String message) {
        super(message);
    }

    public S3FileUploadException(String message, Exception e) {
        super(message);
        super.initCause(e);
    }

}
