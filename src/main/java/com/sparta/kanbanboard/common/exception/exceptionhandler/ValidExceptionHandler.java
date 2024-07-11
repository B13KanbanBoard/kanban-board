package com.sparta.kanbanboard.common.exception.exceptionhandler;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getFieldErrorResponseEntity;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidExceptionHandler {

    /**
     * 유효성 검사 에러 체크
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected <T> ResponseEntity<CommonResponse<T>> validException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : " + e.getMessage());

        return getFieldErrorResponseEntity(e.getBindingResult(), e.getMessage());
    }

}
