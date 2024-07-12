package com.sparta.kanbanboard.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CardUpdateDateResponse {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}
