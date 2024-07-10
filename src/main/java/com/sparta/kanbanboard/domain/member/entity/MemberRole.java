package com.sparta.kanbanboard.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    USER("USER"),
    ADMIN("ADMIN");

    private final String MemberRole;

    public String getRole() {
        return switch(this) {
            case USER -> "USER";
            case ADMIN -> "ADMIN";
        };
    }

}
