package com.sej.escape.error.exception.validation;

import com.sej.escape.error.ErrorCode;
import com.sej.escape.error.exception.BusinessException;

public class InvalidRequestParamter extends BusinessException {

    public InvalidRequestParamter(String message) {
        super(message, ErrorCode.INVALID_REQUEST_VALUE);
    }
}
