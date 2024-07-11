package com.sparta.kanbanboard.common.exception.errorCode;

import com.sparta.kanbanboard.common.status.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_ARGUMENT_ERROR(StatusCode.BAD_REQUEST.getCode(), "올바르지 않은 파라미터입니다."),
    BAD_REQUEST(StatusCode.BAD_REQUEST.code, "올바르지 않은 요청 정보입니다."),
    INTERNAL_SERVER_ERROR(StatusCode.INTERNAL_SERVER_ERROR.code, "서버 내부 에러가 발생하였습니다."),
    AUTH_USER_FORBIDDEN(StatusCode.FORBIDDEN.getCode(), "접근 권한이 없습니다."),
    METHOD_NOT_ALLOWED(StatusCode.BAD_REQUEST.getCode(), "요청 메서드가 잘 못 되었습니다."),
    NO_RESOURCE_FOUND_EXCEPTION(StatusCode.BAD_REQUEST.getCode(), "요청 메서드가 잘 못 되었습니다."),
    ;

    private final Integer httpStatusCode;
    private final String description;
}
