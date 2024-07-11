package com.sparta.kanbanboard.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCreateResponse {
    private Long categoryId;
    private String name;
    private Long orderNumber;
}
