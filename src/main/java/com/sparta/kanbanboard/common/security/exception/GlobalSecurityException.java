package com.sparta.kanbanboard.common.security.exception;

import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;

public interface GlobalSecurityException {
    ErrorCode getErrorCode();

    String getErrorDescription();
}
