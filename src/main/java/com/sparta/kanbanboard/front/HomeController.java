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
import org.springframework.web.bind.annotation.*;

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

    // 선택한 보드 페이지 (카테코리 및 카드 나열)
    @GetMapping("/api/boards/1")
    public String membersPage() { return "board"; }

    //내 정보 페이지
    @GetMapping("/api/members/myInfo")
    public String myInfoPage() {
        return "myInfo";
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