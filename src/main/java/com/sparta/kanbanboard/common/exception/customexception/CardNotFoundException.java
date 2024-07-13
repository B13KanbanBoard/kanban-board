package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalNotFoundException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class CardNotFoundException extends GlobalNotFoundException {
    public CardNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
