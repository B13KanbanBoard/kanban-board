package com.sparta.kanbanboard.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

}
