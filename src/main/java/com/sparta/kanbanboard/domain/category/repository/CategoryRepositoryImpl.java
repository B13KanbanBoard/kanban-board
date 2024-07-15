package com.sparta.kanbanboard.domain.category.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kanbanboard.domain.category.dto.CategoryResponse;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.entity.QCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryResponse> getCategoryListSortOrderNumber(long boardId){

        QCategory category = QCategory.category;

        // same orderSpecifier is created everytime, -> GC automatically deletes after uses
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, category.orderNumber);

        List<Category> categories = jpaQueryFactory.selectFrom(category)
                .from(category)
                .where(category.board.id.eq(boardId))
                .orderBy(orderSpecifier)
                .fetch();

        return categories.stream()
                .map(m ->
                        CategoryResponse.builder()
                                .id(m.getId())
                                .name(m.getName())
                                .orderNumber(m.getOrderNumber())
                                .build()
                ).collect(Collectors.toList());
    }
}
