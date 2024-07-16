package com.sparta.kanbanboard.front;

import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardRepository;
import com.sparta.kanbanboard.domain.member.dto.SignupRequest;
import com.sparta.kanbanboard.domain.member.dto.SignupResponse;
import com.sparta.kanbanboard.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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
    @GetMapping("/members/login-page")
    public String loginPage() {
        return "login";
    }

    //회원가입 페이지
    @GetMapping("/members/signup")
    public String signupPage() {
        return "signup";
    }


    // 선택한 보드 페이지 (카테코리 및 카드 나열)
    final private BoardRepository boardRepository;
    @GetMapping("/boards/{boardId}")
    public ModelAndView test(@PathVariable Long boardId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("boards"); // 실제 뷰 이름 (boards.html)
        mav.addObject("boardId", boardId); // boardId를 모델에 추가

        Board board = boardRepository.findById(boardId).orElseThrow(()->new IllegalArgumentException("Board not found"));
        mav.addObject("boardName", board.getBoardName()); // 보드 이름 넘겨주기

        return mav;
    }

    //내 정보 페이지
    @GetMapping("/members/myInfo")
    public String myInfoPage() {
        return "myInfo";
    }

    /**
     * 폼 데이터 형식  현재 비활성
     */
    @PostMapping("/members/signup2")
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
            return "redirect:/members/signup";
        }
        SignupResponse response = memberService.createUser(request);
        return "redirect:/members/login-page";
    }


}