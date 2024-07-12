package com.sparta.kanbanboard.domain.board.dto;

import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class BoardRequest {
    private String boardName;
    private String content;

    public Board toEntity(Member member) {
        return Board.builder()
            .member(member)
            .boardName(boardName)
            .content(content)
            .build();
    }

}
