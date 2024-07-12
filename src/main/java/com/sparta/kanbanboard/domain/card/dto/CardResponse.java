package com.sparta.kanbanboard.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CardResponse {
    private Long cardId;

    private String title;

    private String assignee;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;
}
