package com.sparta.kanbanboard.common.security.jwt;

public class JwtConstants {
    // Header KEY 값
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Authorization-refresh";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";
}
