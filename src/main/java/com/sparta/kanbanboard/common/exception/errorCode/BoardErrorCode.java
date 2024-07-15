package com.sparta.kanbanboard.common.exception.errorCode;

import com.sparta.kanbanboard.common.status.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {

    NOT_FOUND_BOARD(StatusCode.NOT_FOUND.code,"보드를 찾을 수 없습니다."),
    INAPPROPRIATE_MEMBER_BOARD(StatusCode.BAD_REQUEST.code, "해당 보드에 대한 권한이 없는 사용자입니다."),
    CANNOT_INVITE_SELF(StatusCode.BAD_REQUEST.code, "자기 자신을 초대할 수 없습니다."),
    CANNOT_DELETE_SELF(StatusCode.BAD_REQUEST.code, "자기 자신을 삭제할 수 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final String description;

}
