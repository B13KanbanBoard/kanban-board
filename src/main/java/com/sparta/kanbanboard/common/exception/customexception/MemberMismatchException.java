package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalMismatchException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class MemberMismatchException extends GlobalMismatchException {

    public MemberMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }

}
