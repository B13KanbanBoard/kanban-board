package com.sparta.kanbanboard.domain.card.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class CardUpdateRequest {
    private String title;

    @Email
    private String assignee;

    private String description;
    private Long orderNumber;
}
