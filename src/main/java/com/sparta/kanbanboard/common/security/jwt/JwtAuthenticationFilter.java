package com.sparta.kanbanboard.common.security.jwt;

import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.NOT_SIGNED_UP_USER;
import static com.sparta.kanbanboard.common.security.jwt.JwtConstants.ACCESS_TOKEN_HEADER;
import static com.sparta.kanbanboard.common.security.jwt.JwtConstants.BEARER_PREFIX;
import static com.sparta.kanbanboard.common.security.jwt.JwtConstants.REFRESH_TOKEN_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.exception.customexception.MemberNotFoundException;
import com.sparta.kanbanboard.domain.member.dto.LoginRequest;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
        setFilterProcessesUrl("/api/members/login");
    }

    /**
     * 로그인 시도
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest requestDto = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 로그인 성공 및 JWT생성
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 JWT 토큰 발행");

        Member member = memberRepository.findByEmail(authResult.getName()).orElseThrow(
                () -> new MemberNotFoundException(NOT_SIGNED_UP_USER)
        );
        MemberRole role = member.getRole();
        String email = member.getEmail();

        // 토큰 생성
        String accessToken = jwtProvider.createAccessToken(email, role);
        String refreshToken = jwtProvider.createRefreshToken(email, role);

        // refresh 토큰 저장
        member.saveRefreshToken(refreshToken.substring(BEARER_PREFIX.length()));
        memberRepository.save(member);

        // 응답 헤더에 토큰 추가
        response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(REFRESH_TOKEN_HEADER, refreshToken);

        // JSON 응답 작성
        writeJsonResponse(response, HttpStatus.OK, "로그인에 성공했습니다.", authResult.getName());
    }

    /**
     * 로그인 실패
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패 : {}", failed.getMessage());

        writeJsonResponse(response, HttpStatus.BAD_REQUEST, "로그인에 실패했습니다.", "");
        response.setStatus(401); // 로그인 실패 Status 전송
    }

    /**
     * HttpResponse write Json Response
     */
    private void writeJsonResponse(HttpServletResponse response, HttpStatus status, String message, String data) throws IOException {
        CommonResponse responseMessage = CommonResponse.of(status.value(), message, data);

        String jsonResponse = new ObjectMapper().writeValueAsString(responseMessage);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }

}
