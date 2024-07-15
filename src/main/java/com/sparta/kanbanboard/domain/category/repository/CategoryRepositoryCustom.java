package com.sparta.kanbanboard.domain.category.repository;

import com.sparta.kanbanboard.domain.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryResponse> getCategoryListSortOrderNumber(long boardId);
}
