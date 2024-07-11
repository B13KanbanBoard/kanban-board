package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalMismatchException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class CommentMismatchException extends GlobalMismatchException {
    public CommentMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
