package com.sparta.kanbanboard.common.exception.exceptionhandler;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getBadRequestResponseEntity;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalDuplicatedException;
import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalMismatchException;
import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalRejectedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalDuplicatedException.class)
    protected <T> ResponseEntity<CommonResponse<T>> globalDuplicatedException(GlobalDuplicatedException e) {

        return getBadRequestResponseEntity(e);
    }

    @ExceptionHandler(GlobalMismatchException.class)
    protected <T> ResponseEntity<CommonResponse<T>> globalMismatchException(GlobalMismatchException e) {

        return getBadRequestResponseEntity(e);
    }

    @ExceptionHandler(GlobalNotFoundException.class)
    protected <T> ResponseEntity<CommonResponse<T>> globalNotFoundException(GlobalNotFoundException e) {

        return getBadRequestResponseEntity(e);
    }

    @ExceptionHandler(GlobalRejectedException.class)
    protected <T> ResponseEntity<CommonResponse<T>> globalRejectedException(GlobalRejectedException e) {

        return getBadRequestResponseEntity(e);
    }

}
