package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalNotFoundException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class BoardNotFoundException extends GlobalNotFoundException {

    public BoardNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
