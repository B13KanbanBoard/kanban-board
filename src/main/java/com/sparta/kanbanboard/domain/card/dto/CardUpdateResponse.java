package com.sparta.kanbanboard.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardUpdateResponse {
    private final Long id;
    private final String title;
    private final String assignee;
    private final String description;
    private final Long orderNumber;
}
