package com.sparta.kanbanboard.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.base.dto.ErrorResponse;
import com.sparta.kanbanboard.common.exception.errorCode.ErrorCode;
import com.sparta.kanbanboard.common.security.errorcode.SecurityErrorCode;
import com.sparta.kanbanboard.common.security.exception.CustomSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j(topic = "인증 예외 필터")
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Jwt 인증 도중 예외 발생");
        Exception exception = (Exception) request.getAttribute("exception");

        if (exception instanceof CustomSecurityException e) {
            sendErrorResponse(response, e.getErrorCode());
            return;
        }

        sendErrorResponse(response, SecurityErrorCode.INVALID_JWT_SIGNATURE);
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        String body = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatusCode());
        response.getWriter().write(body);
    }

}
