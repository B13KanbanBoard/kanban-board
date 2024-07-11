package com.sparta.kanbanboard.common.exception.customexception.globalexception;

import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;

    public GlobalNotFoundException (ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
