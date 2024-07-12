package com.sparta.kanbanboard.domain.category.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryUpdateRequest {
    private String name;
    private Long orderNumber;
}
