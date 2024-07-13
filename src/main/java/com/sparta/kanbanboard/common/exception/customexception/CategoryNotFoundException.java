package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalNotFoundException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class CategoryNotFoundException extends GlobalNotFoundException {
    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
