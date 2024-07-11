package com.sparta.kanbanboard.common.exception.errorCode;

public interface ErrorCode {
    Integer getHttpStatusCode();

    String getDescription();
}
