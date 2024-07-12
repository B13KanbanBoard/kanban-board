package com.sparta.kanbanboard.domain.board.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class MemberBoardRequest {

    private List<Long> memberIdList;

    private Long boardId;
}
