package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalRejectedException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class ReissueTokenFailException extends GlobalRejectedException {

    public ReissueTokenFailException(ErrorCode errorCode) {
        super(errorCode);
    }

}
