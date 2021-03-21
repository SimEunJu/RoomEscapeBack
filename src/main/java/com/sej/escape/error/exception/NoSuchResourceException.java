package com.sej.escape.error.exception;

import com.sej.escape.error.ErrorCode;

import java.util.function.Supplier;

public class NoSuchResourceException extends BusinessException{

    public NoSuchResourceException(String message) {
        super(message, ErrorCode.REQUEST_RESOURCE_NOT_EXIST);
    }
}
