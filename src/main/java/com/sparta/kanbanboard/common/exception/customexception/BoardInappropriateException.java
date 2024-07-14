package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalRejectedException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class BoardInappropriateException extends GlobalRejectedException {

    public BoardInappropriateException (ErrorCode errorCode) {
        super(errorCode);
    }

}
