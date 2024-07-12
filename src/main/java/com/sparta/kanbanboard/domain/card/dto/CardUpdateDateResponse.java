package com.sparta.kanbanboard.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CardUpdateDateResponse {
    private final Long id;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
}
