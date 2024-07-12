package com.sparta.kanbanboard.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CardUpdateOrderRequest {
    @NotBlank
    private Long orderNumber;
}
