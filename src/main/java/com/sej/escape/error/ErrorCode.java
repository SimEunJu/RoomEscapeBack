package com.sej.escape.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_REQUEST_VALUE(400, "C001", "Invalid Request Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Method Not Allowed"),

    FILE_UPLOAD_FAIL(500, "C003", "File Upload Fail"),

    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    REQUEST_RESOURCE_NOT_EXIST(404, "C005", "Request Resource Is Not Exist"),
    RESOURCE_ALREADY_EXIST(400, "C010", "Resource Already Exist"),

    ACCESS_DENIED(401, "C006", "Access is Denied"),
    AUTHENTICATION_FAIL(401, "C007", "Authentication Fail"),
    AUTHENTICATION_REQUIRED(401, "C008", "Authentication Required"),
    UNAUTHORIZED(401, "C009","Unathorized");

    private int status;
    private String code;
    private String message;

    ErrorCode(int status, String code, String message){
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
