package com.sparta.kanbanboard.domain.member.controller;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getResponseEntity;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.member.dto.ProfileResponse;
import com.sparta.kanbanboard.domain.member.dto.SignupRequest;
import com.sparta.kanbanboard.domain.member.dto.SignupResponse;
import com.sparta.kanbanboard.domain.member.dto.UpdatePasswordRequest;
import com.sparta.kanbanboard.domain.member.dto.UpdateProfileRequest;
import com.sparta.kanbanboard.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<CommonResponse<SignupResponse>> createMember (
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse response = memberService.createUser(request);
        return getResponseEntity("회원가입 성공", response);
    }

    /**
     * 로그아웃
     */
    @PatchMapping("/logout")
    public ResponseEntity<CommonResponse<Long>> logout (
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = memberService.logout(userDetails.getMember());
        return getResponseEntity("로그아웃 성공", response);
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<CommonResponse<String>> reissueToken (
            HttpServletRequest request, HttpServletResponse response
    ) {
        String refreshToken = memberService.reissueToken(request, response);
        return getResponseEntity("토큰 재발급 성공", refreshToken);
    }

    /**
     * 프로필 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<ProfileResponse>> getProfile (
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        ProfileResponse response = memberService.getProfile(userDetails.getMember());
        return getResponseEntity("프로필 조회 성공", response);
    }

    /**
     * 프로필 수정
     */
    @PatchMapping("/update-profile")
    public ResponseEntity<CommonResponse<ProfileResponse>> updateProfile (
            @Valid @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ProfileResponse response = memberService.updateProfile(request, userDetails.getMember());
        return getResponseEntity("프로필 수정 성공", response);
    }

    /**
     * 비밀번호 수정
     */
    @PatchMapping("/update-pwd")
    public ResponseEntity<CommonResponse<String>> updatePwd (
            @Valid @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String response = memberService.updatePwd(request, userDetails.getMember());
        return getResponseEntity("비밀번호 변경 성공", response);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<CommonResponse<String>> deleteMember (
            @PathVariable Long memberId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String response = memberService.deleteMember(memberId, userDetails.getMember());
        return getResponseEntity("회원 탈퇴 성공", response);
    }

}
