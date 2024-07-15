package com.sparta.kanbanboard.domain.card.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kanbanboard.domain.card.dto.CardResponse;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.entity.QCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CardResponse> getCardListSortOrderNumber(long categoryId){

        QCard card = QCard.card;

        // same orderSpecifier is created everytime, -> GC automatically deletes after uses
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, card.orderNumber);

        // select vs from
        List<Card> cards = jpaQueryFactory.selectFrom(card)  // 이건 db의 모든 값 조회
                .from(card)
                .where(card.category.id.eq(categoryId))
                .orderBy(orderSpecifier)
                .fetch();

        return cards.stream()
                .map(m ->
                        CardResponse.builder()
                                .cardId(m.getId())
                                .title(m.getTitle())
                                .assignee(m.getAssignee())
                                .description(m.getDescription())
                                .startDate(m.getStartDate())
                                .endDate(m.getEndDate())
                                .orderNumber(m.getOrderNumber())
                                .build()
                ).collect(Collectors.toList());
    }
}
