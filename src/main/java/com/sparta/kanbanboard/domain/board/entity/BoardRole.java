package com.sparta.kanbanboard.domain.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum BoardRole {

    MANAGER("MANAGER"),
    PARTICIPANTS("PARTICIPANTS");

    private final String BoardRole;

    BoardRole(String boardRole) {
        BoardRole = boardRole;
    }

    public String getRole() {
        return switch(this) {
            case MANAGER -> "MANAGER";
            case PARTICIPANTS -> "PARTICIPANTS";
        };
    }
}
