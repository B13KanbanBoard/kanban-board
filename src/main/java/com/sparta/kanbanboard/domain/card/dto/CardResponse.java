package com.sparta.kanbanboard.domain.card.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardResponse {
    private Long cardId;
    private String title;
    private String assignee;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public CardResponse(Long cardId, String title, String assignee, String description, LocalDate startDate, LocalDate endDate) {
        this.cardId = cardId;
        this.title = title;
        this.assignee = assignee;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
