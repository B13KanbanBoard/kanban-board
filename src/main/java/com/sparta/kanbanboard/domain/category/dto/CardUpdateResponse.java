package com.sparta.kanbanboard.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardUpdateResponse {
    private Long id;
    private String title;
    private String assignee;
    private String description;
    private Long orderNumber;
}
