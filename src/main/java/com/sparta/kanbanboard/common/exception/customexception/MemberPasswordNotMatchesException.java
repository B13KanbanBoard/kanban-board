package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalMismatchException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class MemberPasswordNotMatchesException extends GlobalMismatchException {

    public MemberPasswordNotMatchesException(ErrorCode errorCode) {
        super(errorCode);
    }

}
