package com.sparta.kanbanboard.common.security.jwt;


import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.NOT_SIGNED_UP_USER;
import static com.sparta.kanbanboard.common.security.jwt.JwtConstants.ACCESS_TOKEN_HEADER;

import com.sparta.kanbanboard.common.exception.customexception.MemberNotFoundException;
import com.sparta.kanbanboard.common.security.UserDetailsServiceImpl;
import com.sparta.kanbanboard.common.security.errorcode.SecurityErrorCode;
import com.sparta.kanbanboard.common.security.exception.CustomSecurityException;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final MemberRepository memberRepository;

    /**
     * 토큰 검증
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtProvider.getJwtFromHeader(req, ACCESS_TOKEN_HEADER);

        log.info("access token 검증");
        if (StringUtils.hasText(accessToken) && !jwtProvider.validateTokenInternal(accessToken)) {
            log.error("Access Token 검증");

            String email = jwtProvider.getEmailFromClaims(accessToken).getSubject();
            Member findMember = memberRepository.findByEmail(email).orElseThrow(
                    () -> new MemberNotFoundException(NOT_SIGNED_UP_USER)
            );

            if(findMember.getRefreshToken() != null){
                if(email.equals(findMember.getEmail())){
                    log.info("Token 인증 성공");

                    Claims info = jwtProvider.getEmailFromClaims(accessToken);
                    setAuthentication(info.getSubject());
                }
            }else{
                log.error("유효하지 않는 Refersh Token");
                req.setAttribute("exception", new CustomSecurityException(SecurityErrorCode.INVALID_JWT_SIGNATURE));
            }
        }
        filterChain.doFilter(req, res);
    }

    /**
     * 인증 처리
     */
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    /**
     * 인증 객체 생성
     */
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}