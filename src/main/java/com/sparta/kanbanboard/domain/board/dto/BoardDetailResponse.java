package com.sparta.kanbanboard.domain.board.dto;

import lombok.Builder;

@Builder
public record BoardDetailResponse(
        Long boardId, String boardName, String boardContent,
        Long categoryId, String categoryName, Long categoryOrderNumber,
        Long cardId, String cardTitle, Long cardOrderNumber
) {

    public static BoardDetailResponse of(
            Long boardId, String boardName, String boardContent,
            Long categoryId, String categoryName, Long categoryOrderNumber,
            Long cardId, String cardTitle, Long cardOrderNumber
    ) {
        return BoardDetailResponse.builder()
                .boardId(boardId)
                .boardName(boardName)
                .boardContent(boardContent)
                .categoryId(categoryId)
                .categoryName(categoryName)
                .categoryOrderNumber(categoryOrderNumber)
                .cardId(cardId)
                .cardTitle(cardTitle)
                .cardOrderNumber(cardOrderNumber)
                .build();
    }

}
