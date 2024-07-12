package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalRejectedException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class MemberAccessDeniedException extends GlobalRejectedException {
    public MemberAccessDeniedException(ErrorCode errorCode) {super(errorCode);}
}
