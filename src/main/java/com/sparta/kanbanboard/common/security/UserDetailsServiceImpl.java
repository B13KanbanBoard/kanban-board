package com.sparta.kanbanboard.common.security;


import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.NOT_SIGNED_UP_USER;

import com.sparta.kanbanboard.common.exception.customexception.MemberNotFoundException;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(NOT_SIGNED_UP_USER));

        return new UserDetailsImpl(member);
    }
}