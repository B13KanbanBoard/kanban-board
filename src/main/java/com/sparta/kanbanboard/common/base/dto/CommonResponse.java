package com.sparta.kanbanboard.common.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class CommonResponse <T> {
    private final boolean success;
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    protected CommonResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResponse<T> of(T data) {
        return new CommonResponse<>(true, 200, "성공", data);
    }

    public static <T> CommonResponse<T> of(String message) {
        return new CommonResponse<>(true, 200, message, null);
    }

    public static <T> CommonResponse<T> of(String message, T data) {
        return new CommonResponse<>(true, 200, message, data);
    }

    public static <T> CommonResponse<T> of(int code, T data) {
        return new CommonResponse<>(true, code, "성공", data);
    }

    public static <T> CommonResponse<T> of(int code, String message) {
        return new CommonResponse<>(true, code, message, null);
    }

    public static <T> CommonResponse<T> of(int code, String message, T data) {
        return new CommonResponse<>(true, code, message, data);
    }
}
