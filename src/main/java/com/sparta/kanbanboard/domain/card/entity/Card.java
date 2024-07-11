package com.sparta.kanbanboard.domain.card.entity;

import com.sparta.kanbanboard.common.base.entity.Timestamped;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.member.entity.Member;
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

    // member email 로 저장
    private String assignee;

    private String description;

    private Long orderNumber;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Card(String title, String assignee, String description, Long orderNumber){
        this.title = title;
        this.assignee = assignee;
        this.description = description;
        this.orderNumber = orderNumber;
    }


    public void setCategory(Category category){
        this.category = category;
        category.getCardList().add(this);
    }

    public void setMember(Member member){
        this.member = member;
    }


}
