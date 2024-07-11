package com.sparta.kanbanboard.domain.member.service;

import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.ALREADY_LOGOUT;
import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.DUPLICATED_USER;

import com.sparta.kanbanboard.common.exception.customexception.MemberAlreadyLogoutException;
import com.sparta.kanbanboard.common.exception.customexception.MemberDuplicationException;
import com.sparta.kanbanboard.domain.member.dto.SignupRequest;
import com.sparta.kanbanboard.domain.member.dto.SignupResponse;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
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

}
