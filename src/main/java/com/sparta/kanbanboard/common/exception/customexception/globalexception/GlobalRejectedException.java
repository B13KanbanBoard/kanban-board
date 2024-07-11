package com.sparta.kanbanboard.common.exception.customexception.globalexception;

import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalRejectedException extends RuntimeException{

    private final ErrorCode errorCode;

    public GlobalRejectedException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
