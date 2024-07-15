package com.sparta.kanbanboard.common.exception.errorCode;

import com.sparta.kanbanboard.common.status.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    NOT_FOUND_COMMENT(StatusCode.NOT_FOUND.getCode(), "존재하지 않는 댓글입니다."),
    NOT_OWNER_COMMENT(StatusCode.FORBIDDEN.getCode(), "댓글 작성자 본인이 아닙니다."),
    NOT_MATH_CARD_ID(StatusCode.FORBIDDEN.getCode(), "요청한 댓글이 해당 카드에 속해있지 않습니다."),
    ;

    private final Integer httpStatusCode;
    private final String description;
}

