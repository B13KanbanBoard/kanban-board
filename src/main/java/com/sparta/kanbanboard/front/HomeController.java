package com.sparta.kanbanboard.front;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.domain.member.dto.SignupRequest;
import com.sparta.kanbanboard.domain.member.dto.SignupResponse;
import com.sparta.kanbanboard.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getResponseEntity;
@Slf4j(topic = "HomeController")
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    //메인 페이지
    @GetMapping("/")
    public String home() {
        return "index";
    }

    //로그인 페이지
    @GetMapping("/api/members/login-page")
    public String loginPage() {
        return "login";
    }

    //회원가입 페이지
    @GetMapping("/api/members/signup")
    public String signupPage() {
        return "signup";
    }

    /**
     * 폼 데이터 형식  현재 비활성
     */
    @PostMapping("/api/members/signup2")
    public String createMember2 (
            @Valid SignupRequest request, BindingResult bindingResult
    ) {
        log.error("tt");
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/members/signup";
        }
        SignupResponse response = memberService.createUser(request);
        return "redirect:/api/members/login-page";
    }


}