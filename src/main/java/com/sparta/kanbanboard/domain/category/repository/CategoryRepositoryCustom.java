package com.sparta.kanbanboard.domain.category.repository;

import com.sparta.kanbanboard.domain.category.dto.CategoryResponse;
import com.sparta.kanbanboard.domain.category.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryResponse> getCategoryListSortOrderNumber(long boardId);
}
