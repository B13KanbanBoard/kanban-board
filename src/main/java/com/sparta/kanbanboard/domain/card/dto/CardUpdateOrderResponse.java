package com.sparta.kanbanboard.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardUpdateOrderResponse {
    private final Long id;
    private final String title;
    private final Long orderNumber;
}
