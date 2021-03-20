package com.sej.escape.error.exception;

import com.sej.escape.error.ErrorCode;

public class NoSuchResourceException extends BusinessException{

    public NoSuchResourceException(String message) {
        super(message, ErrorCode.REQUEST_RESOURCE_NOT_EXIST);
    }
}
