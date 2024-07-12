package com.sparta.kanbanboard.domain.member.dto;

import com.sparta.kanbanboard.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record ProfileResponse (String email, String name) {

    public static ProfileResponse of(Member member) {
        return ProfileResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
