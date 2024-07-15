package com.sparta.kanbanboard.domain.category.dto;

import com.sparta.kanbanboard.domain.card.dto.CardResponse;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.category.entity.Category;
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

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .orderNumber(category.getOrderNumber())
                .build();
    }
}
