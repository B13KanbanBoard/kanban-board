package com.sparta.kanbanboard.common.security.jwt;


import static com.sparta.kanbanboard.common.security.jwt.JwtConstants.AUTHORIZATION_KEY;
import static com.sparta.kanbanboard.common.security.jwt.JwtConstants.BEARER_PREFIX;

import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j(topic = "JwtProvider")
@Component
@RequiredArgsConstructor
public class JwtProvider {
    // 토큰 만료시간 (30분)
    public static final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;
    // 리프레시 토큰 만료시간 (7일)
    public static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;

    // JWT secret key
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] accessKeyBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(accessKeyBytes);
    }

    // access token 생성
    public String createAccessToken(String email, MemberRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // refresh token 생성
    public String createRefreshToken(String email, MemberRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // header에서 JWT value 가져오기
    public String getJwtFromHeader(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    // 토큰 검증
    public boolean validateTokenInternal(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 Claims 불러오기
    public Claims getEmailFromClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
