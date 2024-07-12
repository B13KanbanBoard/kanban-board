package com.sparta.kanbanboard.domain.card.dto;

import lombok.Getter;

@Getter
public class CardCreateResponse {
    private Long id;
    private String title;

    public CardCreateResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
