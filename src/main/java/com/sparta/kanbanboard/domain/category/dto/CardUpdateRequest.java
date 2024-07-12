package com.sparta.kanbanboard.domain.category.dto;

import lombok.Getter;

@Getter
public class CardUpdateRequest {
    private String title;
    private String assignee;
    private String description;
    private Long orderNumber;
}
