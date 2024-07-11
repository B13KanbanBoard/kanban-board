package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.domain.card.dto.CardCreateRequest;
import com.sparta.kanbanboard.domain.card.dto.CardCreateResponse;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardRepository;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.repository.CategoryRepository;
import com.sparta.kanbanboard.domain.category.service.CategoryService;
import com.sparta.kanbanboard.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CardCreateResponse createCard(Long boardId, Long categoryId, CardCreateRequest req, Member member) {
        categoryService.checkBoardAndCategoryRelation(boardId, categoryId);

        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(NullPointerException::new);
        // 예외처리 수정
        Long orderNum = (long) (tempCategory.getCardList().size() + 1);

        Card card = new Card(req.getTitle(), req.getAssignee(), req.getDescription(), orderNum);
        card.setCategory(tempCategory);
        card.setMember(member);

        cardRepository.save(card);

        return new CardCreateResponse(card.getId(), card.getTitle());
    }
}
