package com.sparta.kanbanboard.domain.card.controller;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.card.dto.CardCreateRequest;
import com.sparta.kanbanboard.domain.card.dto.CardCreateResponse;
import com.sparta.kanbanboard.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CardController {

    private final CardService cardService;

    /**
     * 카테고리 생성
     */
    @PostMapping("/{boardId}/categories/{categoryId}/cards")
    public ResponseEntity<CommonResponse<CardCreateResponse>> createCard(
            @PathVariable Long boardId,
            @PathVariable Long categoryId,
            @RequestBody CardCreateRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CardCreateResponse response = cardService.createCard(boardId, categoryId, req, userDetails.getMember());
        return getResponseEntity("카드 생성 완료", response);
    }
}
