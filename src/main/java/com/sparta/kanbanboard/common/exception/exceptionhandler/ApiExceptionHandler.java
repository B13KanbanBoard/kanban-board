package com.sparta.kanbanboard.common.exception.exceptionhandler;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getBadRequestResponseEntity;

import com.sparta.kanbanboard.common.base.dto.ErrorResponse;
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
public class ApiExceptionHandler {

    @ExceptionHandler(GlobalDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> globalDuplicatedException(GlobalDuplicatedException e) {
        return getBadRequestResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(GlobalMismatchException.class)
    protected ResponseEntity<ErrorResponse> globalMismatchException(GlobalMismatchException e) {

        return getBadRequestResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(GlobalNotFoundException.class)
    protected ResponseEntity<ErrorResponse> globalNotFoundException(GlobalNotFoundException e) {

        return getBadRequestResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(GlobalRejectedException.class)
    protected ResponseEntity<ErrorResponse> globalRejectedException(GlobalRejectedException e) {

        return getBadRequestResponseEntity(e.getErrorCode());
    }

}
