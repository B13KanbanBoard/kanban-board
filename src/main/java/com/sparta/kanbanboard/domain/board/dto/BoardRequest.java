package com.sparta.kanbanboard.domain.board.dto;

import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequest {

    @NotBlank(message = "Board의 제목을 입력하세요.")
    private String boardName;
    @NotBlank(message = "Board의 한 줄 설명을 입력하세요.")
    private String content;

}
