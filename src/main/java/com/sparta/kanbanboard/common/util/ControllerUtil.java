package com.sparta.kanbanboard.common.util;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.status.StatusCode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Slf4j(topic = "Controller")
public final class ControllerUtil {

    private ControllerUtil() {
    }

    public static <T> ResponseEntity<CommonResponse<T>> getFieldErrorResponseEntity(BindingResult bindingResult, String message) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            log.error("{} field : {}", fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(StatusCode.BAD_REQUEST.code)
                .body((CommonResponse<T>) CommonResponse.of(message, fieldErrors));
    }

    public static <T> ResponseEntity<CommonResponse<T>> getBadRequestResponseEntity(Exception e) {
        return ResponseEntity.status(StatusCode.BAD_REQUEST.code)
                .body(CommonResponse.of(e.getMessage()));
    }

    public static <T> ResponseEntity<CommonResponse<T>> getResponseEntity(String message, T response) {

        return ResponseEntity.status(StatusCode.OK.code)
                .body(CommonResponse.of(message, response));
    }

}
