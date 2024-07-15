package com.sparta.kanbanboard.domain.category.entity;

import com.sparta.kanbanboard.common.base.entity.Timestamped;
import com.sparta.kanbanboard.common.exception.customexception.*;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode.*;
import static com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode.BAD_REQUEST;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private Long orderNumber;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cardList = new ArrayList<>();

    public Category(String name, Long orderNumber, Member member, Board board){
        this.name = name;
        this.orderNumber = orderNumber;
        this.member = member;
        this.board = board;
    }

    public void updateName(String name){
        if(name != null) {this.name = name;}
    }

    public void updateOrderNumber(Long orderNumber){
        if(orderNumber != null) {
            checkCategoryOrderNumberDuplicate(orderNumber);
            this.orderNumber = orderNumber;
        }
    }

    /**
     * 해당 카테고리에서 Order number 중복 확인
     */
    public void checkCategoryOrderNumberDuplicate(Long orderNumber) {
        List<Category> categories = this.getBoard().getCategoryList();

        for (Category category : categories) {
            if (Objects.equals(category.getOrderNumber(), orderNumber)) {
                throw new OrderNumberDuplicatedException(DUPLICATED_ORDER_NUMBER);
            }
        }
    }

    /**
     * 해당 카테고리에서 name 중복 확인
     */
    public void checkCategoryNameDuplicate(String name) {
        List<Category> categories = this.getBoard().getCategoryList();

        for (Category category : categories) {
            if (Objects.equals(category.getName(), name)) {
                throw new NameDuplicatedException(DUPLICATED_ORDER_NUMBER);
            }
        }
    }

    /**
     * 카테고리가 해당보드에 연관된것이 맞는지 확인
     */
    public void checkBoardAndCategoryRelation(Long boardId) {
        if(!Objects.equals(this.getBoard().getId(), boardId)){
            throw new PathMismatchException(BAD_REQUEST);
        }
    }
}
