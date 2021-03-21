package com.sej.escape.error.exception.file;

public class LocalFileUploadException extends FileUploadException{

    public LocalFileUploadException(String message) {
        super(message);
    }

    public LocalFileUploadException(String message, Exception e) {
        super(message);
        super.initCause(e);
    }
}
