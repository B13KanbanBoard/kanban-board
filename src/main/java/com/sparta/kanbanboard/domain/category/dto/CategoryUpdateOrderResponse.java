package com.sparta.kanbanboard.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryUpdateOrderResponse {

    private final Long id;
    private final String name;
    private final Long orderNumber;
}
