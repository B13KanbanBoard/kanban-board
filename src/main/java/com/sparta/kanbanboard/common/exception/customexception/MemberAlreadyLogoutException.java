package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalRejectedException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class MemberAlreadyLogoutException extends GlobalRejectedException {

    public MemberAlreadyLogoutException(ErrorCode errorCode) {
        super(errorCode);
    }

}
