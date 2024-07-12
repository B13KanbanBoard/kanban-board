package com.sparta.kanbanboard.domain.board.dto;

import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class BoardResponse {
    private Long id;
    private String boardName;
    private String content;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.content = board.getContent();
        this.boardName = board.getBoardName();
    }

    public BoardResponse(Long id) {
        this.id = id;
    }
}
