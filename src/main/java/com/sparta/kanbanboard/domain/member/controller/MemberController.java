package com.sparta.kanbanboard.domain.member.controller;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getResponseEntity;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.member.dto.SignupRequest;
import com.sparta.kanbanboard.domain.member.dto.SignupResponse;
import com.sparta.kanbanboard.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "MemberController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignupResponse>> createMember(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse response = memberService.createUser(request);
        return getResponseEntity("회원가입 성공", response);
    }

    /**
     * 로그아웃
     */
    @PatchMapping("/logout")
    public ResponseEntity<CommonResponse<Long>> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = memberService.logout(userDetails.getMember());
        return getResponseEntity("로그아웃 성공", response);
    }

}
