package com.sparta.kanbanboard.domain.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardRole {

    MANAGER("MANAGER"),
    PARTICIPANTS("PARTICIPANTS");

    private final String boardRole;

    public String getBoardRole(){
        return switch(this) {
            case MANAGER, PARTICIPANTS -> this.boardRole;
        };
    }
}
