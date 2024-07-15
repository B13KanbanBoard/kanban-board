package com.sparta.kanbanboard.domain.card.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.entity.QCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Card> getCardListSortOrderNumber(long categoryId){

        QCard card = QCard.card;

        // same orderSpecifier is created everytime, -> GC automatically deletes after uses
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, card.orderNumber);

        return jpaQueryFactory.selectFrom(card)
                .from(card)
                .where(card.category.id.eq(categoryId))
                .orderBy(orderSpecifier)
                .fetch();
    }
}
