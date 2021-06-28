package com.sej.escape.error.exception.security;

import com.sej.escape.error.ErrorCode;
import com.sej.escape.error.exception.BusinessException;

public class UnAuthorizedException extends BusinessException {

    public UnAuthorizedException(String message) {
        super(message, ErrorCode.UNATHORIZED);
    }
}
