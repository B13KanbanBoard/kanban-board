package com.sparta.kanbanboard.domain.category.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {
    private final Long id;
    private final String name;
    private final Long orderNumber;

    @Builder
    public CategoryResponse(Long id, String name, Long orderNumber) {
        this.id = id;
        this.name = name;
        this.orderNumber = orderNumber;
    }
}
