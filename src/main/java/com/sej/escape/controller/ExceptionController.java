package com.sej.escape.controller;

import com.sej.escape.error.ErrorCode;
import com.sej.escape.error.ErrorRes;
import com.sej.escape.error.exception.BusinessException;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.error.exception.UnDefinedConstantException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    // @Valid 실패 시
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorRes> handleBindException(BindException e){
        log.error("BindException", e);
        ErrorRes response = new ErrorRes(ErrorCode.INVALID_REQUEST_VALUE, e.getFieldErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @Valid 실패 시
    @ExceptionHandler(UnDefinedConstantException.class)
    public ResponseEntity<ErrorRes> handleUnDefinedConstantException(UnDefinedConstantException e){
        log.error("MethodArgumentNotValidException", e);
        ErrorRes response = new ErrorRes(ErrorCode.INVALID_REQUEST_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @Valid 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException", e);
        ErrorRes response = new ErrorRes(ErrorCode.INVALID_REQUEST_VALUE, e.getFieldErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @RequestBody에서 parameter object로 convert 실패
    // request json이 아예 존재하지 않을 때, convert 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRes> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        log.error("HttpMessageNotReadableException : "+e.getMessage(), e);
        ErrorRes response = new ErrorRes(ErrorCode.INVALID_REQUEST_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @RequestMapping에 해당되는 메소드 없을 때
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorRes> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        log.error("HttpRequestMethodNotSupportedException", e);
        ErrorRes response = new ErrorRes(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // authentication 실패
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorRes> handleBadCredentialsException(BadCredentialsException e){
        log.error("BadCredentialsException", e);
        ErrorRes response = new ErrorRes(ErrorCode.AUTHENTICATION_FAIL);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // authentication 없는데, 필요한 정보 요청했을 때
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorRes> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException e){
        log.error("AuthenticationCredentialsNotFoundException", e);
        ErrorRes response = new ErrorRes(ErrorCode.AUTHENTICATION_REQUIRED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // 권한 없을 때
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorRes> handleAccessDeniedException(AccessDeniedException e){
        log.error("AccessDeniedException", e);
        ErrorRes response = new ErrorRes(ErrorCode.AUTHENTICATION_REQUIRED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchResourceException.class)
    public ResponseEntity<ErrorRes> handleNoSuchResourceException(NoSuchResourceException e){
        log.error("NoSuchResourceException", e);
        ErrorRes response = new ErrorRes(ErrorCode.REQUEST_RESOURCE_NOT_EXIST);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 운영 시 발생하는 오류
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorRes> handleBusinessException(BusinessException e){
        log.error("BusinessException", e);
        ErrorCode errorCode = e.getErrorCode();
        ErrorRes response = new ErrorRes(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    // 기타 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleException(Exception e){
        log.error("Exception", e);
        ErrorRes response = new ErrorRes(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}