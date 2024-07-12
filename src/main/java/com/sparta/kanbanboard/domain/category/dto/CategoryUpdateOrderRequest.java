package com.sparta.kanbanboard.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryUpdateOrderRequest {
    @NotBlank
    private Long orderNumber;
}
