package com.sparta.kanbanboard.domain.board.entity;

import com.sparta.kanbanboard.common.base.entity.Timestamped;
import com.sparta.kanbanboard.domain.category.entity.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Long id;

    @Column(nullable = false)
    private String boardName;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberBoard> memberBoardList = new ArrayList<>();
  
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categoryList = new ArrayList<>();


    @Builder(access = AccessLevel.PRIVATE)
    private Board (String boardName, String content) {
        this.boardName = boardName;
        this.content = content;
    }

    /**
     * Board 생성
     */
    public static Board createBoard (String boardName, String content) {
        return Board.builder()
                .boardName(boardName)
                .content(content)
                .build();
    }

    /**
     * Board 수정
     */
    public void updateBoard (String boardName, String content) {
        this.boardName = boardName;
        this.content = content;
    }

    /**
     * 보드 멤버 추가
     */
    public void addMemberBoard(MemberBoard memberBoard) {
        this.memberBoardList.add(memberBoard);
    }
}
