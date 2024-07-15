package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalDuplicatedException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class OrderNumberDuplicatedException extends GlobalDuplicatedException {
    public OrderNumberDuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
