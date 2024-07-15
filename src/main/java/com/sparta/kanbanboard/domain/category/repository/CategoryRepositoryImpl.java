package com.sparta.kanbanboard.domain.category.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.entity.QCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Category> getCategoryListSortOrderNumber(long boardId){

        QCategory category = QCategory.category;

        // same orderSpecifier is created everytime, -> GC automatically deletes after uses
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, category.orderNumber);

        return jpaQueryFactory.selectFrom(category)
                .from(category)
                .where(category.board.id.eq(boardId))
                .orderBy(orderSpecifier)
                .fetch();
    }
}
