package com.sparta.kanbanboard.domain.member.service;

import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.DUPLICATED_USER;
import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.NOT_FOUND_USER;
import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.PASSWORD_NOT_MATCH;
import static com.sparta.kanbanboard.common.security.errorcode.SecurityErrorCode.INVALID_JWT_SIGNATURE;
import static com.sparta.kanbanboard.common.security.jwt.JwtConstants.ACCESS_TOKEN_HEADER;
import static com.sparta.kanbanboard.common.security.jwt.JwtConstants.REFRESH_TOKEN_HEADER;

import com.sparta.kanbanboard.common.exception.customexception.MemberDuplicationException;
import com.sparta.kanbanboard.common.exception.customexception.MemberNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.MemberPasswordNotMatchesException;
import com.sparta.kanbanboard.common.exception.customexception.ReissueTokenFailException;
import com.sparta.kanbanboard.common.security.jwt.JwtProvider;
import com.sparta.kanbanboard.domain.member.dto.ProfileResponse;
import com.sparta.kanbanboard.domain.member.dto.SignupRequest;
import com.sparta.kanbanboard.domain.member.dto.SignupResponse;
import com.sparta.kanbanboard.domain.member.dto.UpdatePasswordRequest;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j(topic = "MemberService")
@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 중복확인
     */
    private void validEmail(String email) {
        if(memberRepository.findByEmail(email).isPresent()){
            throw new MemberDuplicationException(DUPLICATED_USER);
        }
    }

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse createUser(SignupRequest request) {
        validEmail(request.getEmail());

        String password = passwordEncoder.encode(request.getPassword());
        Member  member = Member.createMember(request.getEmail(), password, request.getName(), MemberRole.USER);

        memberRepository.save(member);

        return SignupResponse.of(member);
    }

    /**
     * 로그아웃
     */
    @Transactional
    public Long logout(Member member) {
        member.deleteToken();
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 토큰 재발급
     */
    public String reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtProvider.getJwtFromHeader(request, REFRESH_TOKEN_HEADER);

        if(!StringUtils.hasText(refreshToken) || !jwtProvider.validateTokenInternal(refreshToken)){
            throw new ReissueTokenFailException(INVALID_JWT_SIGNATURE);
        }
        String email = jwtProvider.getEmailFromClaims(refreshToken).getSubject();
        Member findMember = memberRepository.findByEmail(email).orElseThrow(
                () -> new MemberNotFoundException(NOT_FOUND_USER)
        );

        String newAccessToken = jwtProvider.createAccessToken(email, findMember.getRole());
        String newRefreshToken = jwtProvider.createRefreshToken(email, findMember.getRole());

        findMember.setRefreshToken(newRefreshToken);

        response.setHeader(ACCESS_TOKEN_HEADER, newAccessToken);
        response.setHeader(REFRESH_TOKEN_HEADER, newRefreshToken);

        return newRefreshToken;
    }

    /**
     * 프로필 조회
     */
    public ProfileResponse getProfile(Member member) {
        return ProfileResponse.of(member);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public String updatePwd(UpdatePasswordRequest request, Member member) {
        if(!passwordEncoder.matches(member.getPassword(), request.getOldPassword())){
            throw new MemberPasswordNotMatchesException(PASSWORD_NOT_MATCH);
        }
        String newPassword = passwordEncoder.encode(request.getNewPassword());

        member.updatePwd(newPassword);
        memberRepository.save(member);

        return member.getEmail();
    }
}
