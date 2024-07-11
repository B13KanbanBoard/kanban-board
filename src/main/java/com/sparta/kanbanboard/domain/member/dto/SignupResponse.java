package com.sparta.kanbanboard.domain.member.dto;

import com.sparta.kanbanboard.domain.member.entity.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record SignupResponse(Long id, String email, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static SignupResponse of(Member member) {
        return SignupResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

}
