package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.common.exception.customexception.*;
import com.sparta.kanbanboard.domain.card.dto.*;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardRepository;
import com.sparta.kanbanboard.domain.card.dto.CardUpdateRequest;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.repository.CategoryRepository;
import com.sparta.kanbanboard.domain.category.service.CategoryService;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode.*;

@Service
@Slf4j(topic = "CardService")
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    /**
     * 카드 생성
     */
    @Transactional
    public CardCreateResponse createCard(Long categoryId, CardCreateRequest req, Member member) {

        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));
        Long orderNum = (long) (tempCategory.getCardList().size() + 1);

        Card card = new Card(req.getTitle(), orderNum, tempCategory, member);

        cardRepository.save(card);

        return new CardCreateResponse(card.getId(), card.getTitle());
    }

    /**
     * 카드 전체 조회
     */
    public List<CardResponse> getAllCards(Long categoryId, Member member) {

        return cardRepository.getCardListSortOrderNumber(categoryId);
    }

    /**
     * 카드 조회
     */
    public CardResponse getCard(Long categoryId, Long cardId, Member member) {
        Card tempCard = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException(CARD_NOT_FOUND));
        tempCard.checkCategoryAndCardRelation(categoryId);

        return new CardResponse(tempCard.getId(), tempCard.getTitle(), tempCard.getAssignee(), tempCard.getDescription(),
                tempCard.getStartDate(), tempCard.getEndDate(), tempCard.getOrderNumber());
    }

    /**
     * 카드 수정
     */
    @Transactional
    public CardResponse updateCard(Long categoryId, Long cardId, CardUpdateRequest req, Member member) {
        Card tempCard = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException(CARD_NOT_FOUND));
        tempCard.checkCategoryAndCardRelation(categoryId);
        checkMemberAuthToCard(member, tempCard);

        String title = req.getTitle();
        String assignee = req.getAssignee();
        String description = req.getDescription();
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();

        tempCard.updateCard(title, assignee, description, startDate, endDate);

        return new CardResponse(tempCard.getId(), tempCard.getTitle(), tempCard.getAssignee(),
                tempCard.getDescription(), tempCard.getStartDate(), tempCard.getEndDate(), tempCard.getOrderNumber());
    }

    /**
     * 카드 orderNumber 수정
     */
    @Transactional
    public CardUpdateOrderResponse updateOrderNumberCard(Long categoryId, Long cardId,
                                                         CardUpdateOrderRequest req, Member member) {
        Card tempCard = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException(CARD_NOT_FOUND));
        tempCard.checkCategoryAndCardRelation(categoryId);
        checkMemberAuthToCard(member, tempCard);

        Long orderNum = req.getOrderNumber();
        tempCard.updateOrderNumber(orderNum);

        return new CardUpdateOrderResponse(tempCard.getId(), tempCard.getTitle(), tempCard.getOrderNumber());
    }

    /**
     * 카드 삭제
     */
    @Transactional
    public void deleteCard(Long categoryId, Long cardId, Member member) {
        Card tempCard = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException(CARD_NOT_FOUND));
        tempCard.checkCategoryAndCardRelation(categoryId);
        checkMemberAuthToCard(member, tempCard);
        cardRepository.delete(tempCard);
    }

    /**
     * 멤버가 카드 생성자 또는 보드 생성자인지 확인
     */
    public void checkMemberAuthToCard(Member member, Card card){
        if( (!Objects.equals(card.getMember().getId(), member.getId())) && (!member.getRole().equals(MemberRole.ADMIN)) ){
            // 멤버롤 대신 멤버보드에서 롤이 생성자가 맞는지 확인하는 로직으로 변경 필요
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }
    }
}
