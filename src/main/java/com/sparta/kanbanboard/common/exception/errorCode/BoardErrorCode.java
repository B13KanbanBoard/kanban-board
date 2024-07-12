package com.sparta.kanbanboard.common.exception.errorCode;

import com.sparta.kanbanboard.common.status.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {

    NOT_FOUND_BOARD(StatusCode.NOT_FOUND.code,"보드를 찾을 수 없습니다.");

    private final Integer httpStatusCode;
    private final String description;

}
