package com.sej.escape.error.exception.file;

import com.sej.escape.error.ErrorCode;
import com.sej.escape.error.exception.BusinessException;

public class UnsupportedUploadManageType extends BusinessException {

    public UnsupportedUploadManageType(ErrorCode errorCode) {
        super(errorCode);
    }
}
