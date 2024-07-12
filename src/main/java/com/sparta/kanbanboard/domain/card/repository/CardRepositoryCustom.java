package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.domain.card.entity.Card;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepositoryCustom {
    List<Card> getCardListSortOrderNumber(long categoryId);
}
