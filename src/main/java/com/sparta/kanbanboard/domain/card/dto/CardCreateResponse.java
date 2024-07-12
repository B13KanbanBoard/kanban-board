package com.sparta.kanbanboard.domain.card.dto;

import lombok.Getter;

@Getter
public class CardCreateResponse {
    private final Long id;
    private final String title;

    public CardCreateResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
