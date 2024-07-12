package com.sparta.kanbanboard.domain.category.dto;

import lombok.Getter;

@Getter
public class CategoryUpdateRequest {
    private String name;
    private Long orderNumber;
}
