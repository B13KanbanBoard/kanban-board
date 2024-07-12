package com.sparta.kanbanboard.domain.board.entity;

import com.sparta.kanbanboard.common.base.entity.Timestamped;
import com.sparta.kanbanboard.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    private Member member;

    @Column(nullable = false)
    private String boardName;

    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberBoard> memberBoardList = new ArrayList<>();

    @Builder
    public Board(Member member, String boardName, String content) {
        this.member = member;
        this.boardName = boardName;
        this.content = content;
    }

    public void update(String boardName, String content) {
        this.boardName = boardName;
        this.content = content;
    }

    public void addMemberBoard(MemberBoard memberBoard) {
        this.memberBoardList.add(memberBoard);
    }
}
