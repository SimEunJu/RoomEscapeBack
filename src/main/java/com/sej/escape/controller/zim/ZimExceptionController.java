package com.sej.escape.controller.zim;

import com.sej.escape.dto.zim.ZimResDto;
import com.sej.escape.error.ErrorCode;
import com.sej.escape.error.ErrorRes;
import com.sej.escape.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

@RestControllerAdvice(assignableTypes = {ZimController.class})
@Slf4j
@ApiIgnore
// TODO: 실패 시 찜을 위한 정의된 규격의 response를 보내기 위해 ZimController에서 발생하는 실패만 관리하려 했으나
// 해당 컨트롤러를 실행하지 않고 글로벌 컨트롤러를 실행
public class ZimExceptionController {

    // 운영 시 발생하는 오류
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ZimResDto> handleBusinessException(BusinessException e){
        log.error("BusinessException", e);
        ErrorCode errorCode = e.getErrorCode();
        ErrorRes response = new ErrorRes(errorCode);
        ZimResDto resDto = getResDto(response);
        return new ResponseEntity<>(resDto, HttpStatus.valueOf(errorCode.getStatus()));
    }

    // 기타 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ZimResDto> handleException(Exception e){
        log.error("Exception", e);
        ErrorRes response = new ErrorRes(ErrorCode.INTERNAL_SERVER_ERROR);
        ZimResDto resDto = getResDto(response);
        return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ZimResDto getResDto(ErrorRes errorRes){
        return ZimResDto.builder()
                .hasError(true)
                .error(errorRes)
                .build();
    }

}

