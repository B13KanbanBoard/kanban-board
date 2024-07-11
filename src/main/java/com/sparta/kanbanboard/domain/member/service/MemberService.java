package com.sparta.kanbanboard.domain.member.service;

import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "MemberService")
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

}
