package com.sparta.kanbanboard.domain.card.entity;

import com.sparta.kanbanboard.common.base.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "card")
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String assignee;

    private String description;

//    @ManyToOne
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;
//
//    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments;

}
