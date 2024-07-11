package com.sparta.kanbanboard.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.base.dto.ErrorResponse;
import com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode;
import com.sparta.kanbanboard.common.status.StatusCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(CommonErrorCode.AUTH_USER_FORBIDDEN);

        String body = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(StatusCode.FORBIDDEN.code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
    }
}
