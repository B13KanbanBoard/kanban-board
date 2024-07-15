package com.sparta.kanbanboard.domain.board.entity;

import static com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode.INAPPROPRIATE_MEMBER_BOARD;

import com.sparta.kanbanboard.common.exception.customexception.BoardInappropriateException;
import com.sparta.kanbanboard.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class MemberBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardRole role;

    @Builder(access = AccessLevel.PRIVATE)
    private MemberBoard(Member member, Board board, BoardRole boardRole) {
        this.member = member;
        this.board = board;
        this.role = boardRole;
    }

    /**
     * MemberBoard 생성
     */
    public static MemberBoard createMemberBoard (Member member, Board board, BoardRole boardRole) {
        return MemberBoard.builder()
                .member(member)
                .board(board)
                .boardRole(boardRole)
                .build();
    }

    /**
     * 로그인 유저의 Board 권한 체크
     */
    public void validBoardRole(){
        if(BoardRole.PARTICIPANTS.equals(this.role)){
            throw new BoardInappropriateException(INAPPROPRIATE_MEMBER_BOARD);
        }
    }
}
