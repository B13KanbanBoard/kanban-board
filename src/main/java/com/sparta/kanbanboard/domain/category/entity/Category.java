package com.sparta.kanbanboard.domain.category.entity;

import com.sparta.kanbanboard.common.base.entity.Timestamped;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Long orderNumber;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cardList = new ArrayList<>();

    public Category(String name, Long orderNumber){
        this.name = name;
        this.orderNumber = orderNumber;
    }

    public void setMember(Member member){
        this.member = member;
    }

    public void setBoard(Board board){
        this.board = board;
        board.getCategoryList().add(this);
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateOrderNumber(Long orderNumber){
        this.orderNumber = orderNumber;
    }

}
