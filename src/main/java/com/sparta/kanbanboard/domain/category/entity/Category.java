package com.sparta.kanbanboard.domain.category.entity;

import com.sparta.kanbanboard.common.base.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long order;

//    @ManyToOne
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;
//
//    @ManyToOne
//    @JoinColumn(name = "board_id", nullable = false)
//    private Board board;

}
