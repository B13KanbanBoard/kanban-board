package com.sparta.kanbanboard.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CardCreateRequest {
    @NotBlank
    private String title;

    private String assignee;
    private String description;
}
