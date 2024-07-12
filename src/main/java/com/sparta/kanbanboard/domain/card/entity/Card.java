package com.sparta.kanbanboard.domain.card.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    // member email 로 저장
    @Column(nullable = false)
    private String assignee;

    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(nullable = false, unique = true)
    private Long orderNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Card(String title, Long orderNumber, Category category, Member member){
        this.title = title;
        this.orderNumber = orderNumber;
        this.category = category;
        this.member = member;
    }

    public void updateStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    public void updateEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

    public void updateTitle(String title){
        this.title = title;
    }

    public void updateDescription(String description){
        this.description = description;
    }

    public void updateAssignee(String assignee){
        this.assignee = assignee;
    }

    public void updateOrderNumber(Long orderNumber){
        this.orderNumber = orderNumber;
    }

}
