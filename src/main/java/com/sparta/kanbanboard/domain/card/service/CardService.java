package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.common.exception.customexception.BoardNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.CardNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.CategoryNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.MemberAccessDeniedException;
import com.sparta.kanbanboard.domain.board.entity.MemberBoard;
import com.sparta.kanbanboard.domain.board.repository.MemberBoardRepository;
import com.sparta.kanbanboard.domain.card.dto.*;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardRepository;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.repository.CategoryRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode.NOT_FOUND_BOARD;
import static com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode.*;

@Service
@Slf4j(topic = "CardService")
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final MemberBoardRepository memberBoardRepository;

    /**
     * 카드 생성
     */
    @Transactional
    public CardCreateResponse createCard(Long categoryId, CardCreateRequest req, Member member) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        // 보드 참여자인지 확인
        if(!memberBoardRepository.existsByBoardIdAndMemberId(tempCategory.getBoard().getId(), member.getId())){
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }

        Long orderNum = (long) (tempCategory.getCardList().size() + 1);
        Card card = new Card(req.getTitle(), orderNum, tempCategory, member);
        // 카드 순서 중복 확인
        card.checkCardOrderNumberDuplicate(orderNum);

        cardRepository.save(card);

        return new CardCreateResponse(card.getId(), card.getTitle());
    }

    /**
     * 카드 전체 조회
     */
    public List<CardResponse> getAllCards(Long categoryId, Member member) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND)
        );
        // 보드 참여자인지 확인
        if(!memberBoardRepository.existsByBoardIdAndMemberId(tempCategory.getBoard().getId(), member.getId())){
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }

        return cardRepository.getCardListSortOrderNumber(categoryId);
    }

    /**
     * 카드 조회
     */
    public CardResponse getCard(Long categoryId, Long cardId, Member member) {
        Card tempCard = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException(CARD_NOT_FOUND));
        // 보드 참여자인지 확인
        if(!memberBoardRepository.existsByBoardIdAndMemberId(tempCard.getCategory().getBoard().getId(), member.getId())){
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }

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
        // 유저 권한 확인
        checkMemberAuthToCard(tempCard, member.getId());

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
        // 유저 권한 확인
        checkMemberAuthToCard(tempCard, member.getId());

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
        // 유저 권한 확인
        checkMemberAuthToCard(tempCard, member.getId());
        cardRepository.delete(tempCard);
    }


    /**
     * 멤버가 카드 생성자 또는 보드 생성자인지 확인
     */
    public void checkMemberAuthToCard(Card card, Long memberId){
        MemberBoard memberBoard = memberBoardRepository.findByBoardIdAndMemberId(card.getCategory().getBoard().getId(), memberId).orElseThrow(
                () -> new BoardNotFoundException(NOT_FOUND_BOARD)
        );
        if( (!Objects.equals(card.getMember().getId(), memberId)) && (!memberBoard.checkIfManager()) ){
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }
    }
}
