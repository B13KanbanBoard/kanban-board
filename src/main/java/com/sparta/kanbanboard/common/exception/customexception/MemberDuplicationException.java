package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalDuplicatedException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class MemberDuplicationException extends GlobalDuplicatedException {

    public MemberDuplicationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
