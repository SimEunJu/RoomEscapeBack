package com.sej.escape.error.exception;

import com.sej.escape.error.ErrorCode;

public class UnDefinedConstantException extends BusinessException {

    public UnDefinedConstantException(String message) {
        super(message, ErrorCode.INVALID_REQUEST_VALUE);
    }
}
