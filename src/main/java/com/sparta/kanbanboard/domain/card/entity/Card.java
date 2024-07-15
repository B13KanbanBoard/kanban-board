package com.sparta.kanbanboard.domain.card.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.kanbanboard.common.exception.customexception.CategoryNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.OrderNumberDuplicatedException;
import com.sparta.kanbanboard.common.exception.customexception.PathMismatchException;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode.*;

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
    @Column(nullable = false)
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


    public void updateOrderNumber(Long orderNumber){
        if(orderNumber != null) {
            checkCardOrderNumberDuplicate(orderNumber);
            this.orderNumber = orderNumber;
        }
    }

    public void updateCard(String title, String assignee, String description, LocalDate startDate, LocalDate endDate){
        if(title != null) {this.title = title;}
        if(assignee != null) {this.assignee = assignee;}
        if(description != null) {this.description = description;}
        if(startDate != null) {this.startDate = startDate;}
        if(endDate != null) {this.endDate = endDate;}
    }

    /**
     * 카드 순서 중복 확인
     */
    public void checkCardOrderNumberDuplicate(Long orderNumber){
        List<Card> cards = this.getCategory().getCardList();

        for (Card card : cards) {
            if (Objects.equals(card.getOrderNumber(), orderNumber)) {
                throw new OrderNumberDuplicatedException(DUPLICATED_ORDER_NUMBER);
            }
        }
    }

    /**
     * 카테고리, 카드 연관 확인
     */
    public void checkCategoryAndCardRelation(Long categoryId) {
        if(!Objects.equals(this.getCategory().getId(), categoryId)){
            throw new PathMismatchException(BAD_REQUEST);
        }
    }

}
