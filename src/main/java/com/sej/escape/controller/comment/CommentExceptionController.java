package com.sej.escape.controller.comment;

import com.sej.escape.dto.comment.CommentResDto;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.error.ErrorCode;
import com.sej.escape.error.ErrorRes;
import com.sej.escape.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {CommentController.class, StoreCommentController.class, BoardCommentController.class})
@Slf4j
public class CommentExceptionController {

    // 운영 시 발생하는 오류
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommentResDto> handleBusinessException(BusinessException e){
        log.error("BusinessException", e);
        ErrorCode errorCode = e.getErrorCode();
        ErrorRes response = new ErrorRes(errorCode);
        CommentResDto resDto = getResDto(response);
        return new ResponseEntity<>(resDto, HttpStatus.valueOf(errorCode.getStatus()));
    }

    // 기타 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommentResDto> handleException(Exception e){
        log.error("Exception", e);
        ErrorRes response = new ErrorRes(ErrorCode.INTERNAL_SERVER_ERROR);
        CommentResDto resDto = getResDto(response);
        return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private CommentResDto getResDto(ErrorRes errorRes){
        return CommentResDto.resBuilder()
                .hasError(true)
                .error(errorRes)
                .build();
    }
}
