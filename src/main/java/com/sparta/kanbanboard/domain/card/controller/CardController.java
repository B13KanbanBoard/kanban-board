package com.sparta.kanbanboard.domain.card.controller;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.card.dto.*;
import com.sparta.kanbanboard.domain.card.service.CardService;
import com.sparta.kanbanboard.domain.card.dto.CardUpdateRequest;
import com.sparta.kanbanboard.domain.card.dto.CardUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CardController {

    private final CardService cardService;

    /**
     * 카드 생성
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

    /**
     * 카드 전체 조회
     */
    @GetMapping("/{boardId}/categories/{categoryId}/cards")
    public ResponseEntity<CommonResponse<List<CardResponse>>> getAllCards(
            @PathVariable Long boardId,
            @PathVariable Long categoryId
    ){
        List<CardResponse> response = cardService.getAllCards(boardId, categoryId);
        return getResponseEntity("카드 전체 조회 완료", response);
    }

    /**
     * 특정 카드 조회
     */
    @GetMapping("/{boardId}/categories/{categoryId}/cards/{cardId}")
    public ResponseEntity<CommonResponse<CardResponse>> getCard(
            @PathVariable Long boardId,
            @PathVariable Long categoryId,
            @PathVariable Long cardId
    ){
        CardResponse response = cardService.getCard(boardId, categoryId, cardId);
        return getResponseEntity("카드 조회 완료", response);
    }


    /**
     * 시작일자, 마감일자 설정 :
     */
    @PatchMapping("/{boardId}/categories/{categoryId}/cards/{cardId}/update-date")
    public ResponseEntity<CommonResponse<CardUpdateDateResponse>> updateDateCard(
            @PathVariable Long boardId,
            @PathVariable Long categoryId,
            @PathVariable Long cardId,
            @RequestBody CardUpdateDateRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CardUpdateDateResponse response = cardService.updateDateCard(boardId, categoryId, cardId, req, userDetails.getMember());
        return getResponseEntity("시작/마감 일자 수정 완료", response);
    }

    /**
     * 카드 수정
     */
    @PatchMapping("/{boardId}/categories/{categoryId}/cards/{cardId}")
    public ResponseEntity<CommonResponse<CardUpdateResponse>> updateCard(
            @PathVariable Long boardId,
            @PathVariable Long categoryId,
            @PathVariable Long cardId,
            @RequestBody CardUpdateRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CardUpdateResponse response = cardService.updateCard(boardId, categoryId, cardId, req, userDetails.getMember());
        return getResponseEntity("카드 수정 완료", response);
    }

    /**
     * 카드 삭제
     */
    @DeleteMapping("/{boardId}/categories/{categoryId}/cards/{cardId}")
    public ResponseEntity<CommonResponse<Long>> deleteCard(
            @PathVariable Long boardId,
            @PathVariable Long categoryId,
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        cardService.deleteCard(boardId, categoryId, cardId, userDetails.getMember());
        return getResponseEntity("카드 삭제 완료", cardId);
    }
}
