package com.sparta.kanbanboard.common.exception.customexception;

import com.sparta.kanbanboard.common.exception.customexception.globalexception.GlobalMismatchException;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public class PathMismatchException extends GlobalMismatchException {
    public PathMismatchException(ErrorCode errorCode) {super(errorCode);}
}
