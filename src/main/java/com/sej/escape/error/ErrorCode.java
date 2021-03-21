package com.sej.escape.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_REQUEST_VALUE(400, "C001", "Invalid Request Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Method Not Allowed"),
    MISSING_REQUEST_VALUE(400, "C003", "Missing Request Value"),

    FILE_UPLOAD_FAIL(500, "C008", "FILE UPLOAD FAIL"),

    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    REQUEST_RESOURCE_NOT_EXIST(404, "C005", "Request Resource Is Not Exist"),

    ACCESS_DENIED(401, "C006", "Access is Denied"),
    AUTHENTICATION_FAIL(401, "C007", "Authentication Fail");

    private int status;
    private String code;
    private String message;

    ErrorCode(int status, String code, String message){
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
