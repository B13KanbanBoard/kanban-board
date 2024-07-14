package com.sparta.kanbanboard.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class InviteMemberRequest {

    @NotBlank(message = "보드ID를 선택해주세요.")
    private Long boardId;
    @NotEmpty(message = "초대하려는 member를 선택해주세요.")
    private List<Long> memberIdList;

}
