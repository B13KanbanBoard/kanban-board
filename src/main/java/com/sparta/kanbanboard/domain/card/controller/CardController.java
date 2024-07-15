package com.sparta.kanbanboard.domain.card.controller;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.card.dto.*;
import com.sparta.kanbanboard.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    /**
     * 카드 생성
     */
    @PostMapping("/categories/{categoryId}/cards")
    public ResponseEntity<CommonResponse<CardCreateResponse>> createCard(
            @PathVariable Long categoryId,
            @RequestBody CardCreateRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CardCreateResponse response = cardService.createCard(categoryId, req, userDetails.getMember());
        return getResponseEntity("카드 생성 완료", response);
    }

    /**
     * 카드 전체 조회
     */
    @GetMapping("/categories/{categoryId}/cards")
    public ResponseEntity<CommonResponse<List<CardResponse>>> getAllCards(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        List<CardResponse> response = cardService.getAllCards(categoryId, userDetails.getMember());
        return getResponseEntity("카드 전체 조회 완료", response);
    }

    /**
     * 특정 카드 조회
     */
    @GetMapping("categories/{categoryId}/cards/{cardId}")
    public ResponseEntity<CommonResponse<CardResponse>> getCard(
            @PathVariable Long categoryId,
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CardResponse response = cardService.getCard(categoryId, cardId, userDetails.getMember());
        return getResponseEntity("카드 조회 완료", response);
    }

    /**
     * 카드 수정
     */
    @PatchMapping("/categories/{categoryId}/cards/{cardId}")
    public ResponseEntity<CommonResponse<CardResponse>> updateCard(
            @PathVariable Long categoryId,
            @PathVariable Long cardId,
            @RequestBody CardUpdateRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CardResponse response = cardService.updateCard(categoryId, cardId, req, userDetails.getMember());
        return getResponseEntity("카드 수정 완료", response);
    }

    /**
     * 카드 순서 수정
     */
    @PatchMapping("/categories/{categoryId}/cards/{cardId}/update-order")
    public ResponseEntity<CommonResponse<CardUpdateOrderResponse>> updateOrderNumberCard(
            @PathVariable Long categoryId,
            @PathVariable Long cardId,
            @RequestBody CardUpdateOrderRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CardUpdateOrderResponse response = cardService.updateOrderNumberCard(categoryId, cardId, req, userDetails.getMember());
        return getResponseEntity("카드 순서 수정 완료", response);
    }

    /**
     * 카드 삭제
     */
    @DeleteMapping("/categories/{categoryId}/cards/{cardId}")
    public ResponseEntity<CommonResponse<Long>> deleteCard(
            @PathVariable Long categoryId,
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        cardService.deleteCard(categoryId, cardId, userDetails.getMember());
        return getResponseEntity("카드 삭제 완료", cardId);
    }
}
