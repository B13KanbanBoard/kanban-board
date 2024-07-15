package com.sparta.kanbanboard.domain.card.dto;

import com.sparta.kanbanboard.domain.card.entity.Card;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardResponse {
    private final Long cardId;
    private final String title;
    private final String assignee;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long orderNumber;

    @Builder
    public CardResponse(Long cardId, String title, String assignee, String description,
                        LocalDate startDate, LocalDate endDate, Long orderNumber) {
        this.cardId = cardId;
        this.title = title;
        this.assignee = assignee;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.orderNumber = orderNumber;
    }
}
