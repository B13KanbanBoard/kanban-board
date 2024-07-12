package com.sparta.kanbanboard.domain.board.dto;

import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.entity.BoardRole;
import com.sparta.kanbanboard.domain.member.entity.Member;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberBoardResponse {
    private Member memberId;

    private Board boardId;

    private Enum<BoardRole> role;

    public MemberBoardResponse(Member member, Board board, BoardRole boardRole) {
        this.memberId = member;
        this.boardId = board;
        this.role = boardRole;
    }
}
