package com.sparta.kanbanboard.common.exception.errorCode;

import com.sparta.kanbanboard.common.status.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    DUPLICATED_USER(StatusCode.BAD_REQUEST.getCode(), "중복된 유저 정보입니다."),

    NOT_SIGNED_UP_USER(StatusCode.NOT_FOUND.getCode(), "회원가입 후 이용해 주세요."),

    NOT_AUTHORITY_TO_REGISTER(StatusCode.UNAUTHORIZED.getCode(), "등록 권한이 없습니다."),
    NOT_FOUND_USER(StatusCode.NOT_FOUND.getCode(), "아이디 또는 비밀번호를 잘못 입력했습니다."),
    PASSWORD_NOT_MATCH(StatusCode.BAD_REQUEST.getCode(), "비밀번호가 일치하지 않습니다."),
    USER_STATUS_DISABLE(StatusCode.BAD_REQUEST.getCode(), "이미 탈퇴한 사용자입니다."),
    PASSWORD_MATCH(StatusCode.BAD_REQUEST.getCode(), "최근 사용한 4개의 비밀번호를 사용할 수 없습니다."),
    ;

    private final Integer httpStatusCode;
    private final String description;
}

