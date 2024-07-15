package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.domain.card.dto.CardResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepositoryCustom {
    List<CardResponse> getCardListSortOrderNumber(long categoryId);
}
