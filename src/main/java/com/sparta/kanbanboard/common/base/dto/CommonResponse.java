package com.sparta.kanbanboard.common.base.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private final int statusCode;
    private final String message;
    private final T data;

    @Builder
    public CommonResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
