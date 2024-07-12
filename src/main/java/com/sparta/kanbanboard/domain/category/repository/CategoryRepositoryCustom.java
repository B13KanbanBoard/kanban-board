package com.sparta.kanbanboard.domain.category.repository;

import com.sparta.kanbanboard.domain.category.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> getCategoryListSortOrderNumber(long boardId);
}
