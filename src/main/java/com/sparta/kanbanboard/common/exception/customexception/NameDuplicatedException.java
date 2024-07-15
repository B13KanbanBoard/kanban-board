package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalDuplicatedException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class NameDuplicatedException extends GlobalDuplicatedException {
    public NameDuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}