package com.sej.escape.error;

import lombok.*;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ErrorRes {

    private String code;
    private int status;
    private String message;
    private List<FieldError> errors;

    public ErrorRes(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.errors = new ArrayList<>();
    }

    public ErrorRes(ErrorCode errorCode, List<FieldError> errors){
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.errors = errors;
    }
}
