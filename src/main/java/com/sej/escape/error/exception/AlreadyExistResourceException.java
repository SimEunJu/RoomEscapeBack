package com.sej.escape.error.exception;

import com.sej.escape.error.ErrorCode;

public class AlreadyExistResourceException extends BusinessException{
    public AlreadyExistResourceException(String message) {
        super(message, ErrorCode.RESOURCE_ALREADY_EXIST);
    }
}
