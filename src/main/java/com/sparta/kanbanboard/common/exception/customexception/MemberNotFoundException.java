package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalNotFoundException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class MemberNotFoundException extends GlobalNotFoundException {
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
