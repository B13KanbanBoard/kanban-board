package com.sparta.kanbanboard.domain.board.dto;

import com.sparta.kanbanboard.domain.board.entity.Board;
import lombok.Builder;

@Builder
public record BoardResponse(Long boardId, String boardName, String content) {

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
                .boardId(board.getId())
                .boardName(board.getBoardName())
                .content(board.getContent())
                .build();
    }
}
