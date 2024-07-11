package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalNotFoundException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class CommentNotFoundException extends GlobalNotFoundException {
    public CommentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
