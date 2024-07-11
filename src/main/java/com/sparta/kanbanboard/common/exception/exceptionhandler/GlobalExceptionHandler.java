package com.sparta.kanbanboard.common.exception.exceptionhandler;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getBadRequestResponseEntity;

import com.sparta.kanbanboard.common.base.dto.ErrorResponse;
import com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 요청 정보가 잘못된 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("{}", e.getMessage());

        return getBadRequestResponseEntity(CommonErrorCode.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("{}", e.getMessage());

        return getBadRequestResponseEntity(CommonErrorCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 지원하지 않는 API 요청인 경우
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("[handleNoResourceFoundException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        log.error("{}", e.getMessage());

        return getBadRequestResponseEntity(CommonErrorCode.NO_RESOURCE_FOUND_EXCEPTION);
    }

    /**
     * 하위에서 잡지 못한 에러
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) {
        log.error("[handleException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        log.error("{}", e.getMessage());

        return getBadRequestResponseEntity(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

}
