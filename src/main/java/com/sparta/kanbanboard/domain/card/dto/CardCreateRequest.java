package com.sparta.kanbanboard.domain.card.dto;

import lombok.Getter;

@Getter
public class CardCreateRequest {
    private String title;
    private String assignee;
    private String description;
}
