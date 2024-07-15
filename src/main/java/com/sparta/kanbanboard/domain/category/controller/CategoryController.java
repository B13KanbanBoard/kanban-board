package com.sparta.kanbanboard.domain.category.controller;


import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.category.dto.CategoryCreateRequest;
import com.sparta.kanbanboard.domain.category.dto.CategoryResponse;
import com.sparta.kanbanboard.domain.category.dto.CategoryUpdateOrderRequest;
import com.sparta.kanbanboard.domain.category.dto.CategoryUpdateRequest;
import com.sparta.kanbanboard.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 생성
     */
    @PostMapping("/{boardId}/categories")
    public ResponseEntity<CommonResponse<CategoryResponse>> createCategory(
            @PathVariable Long boardId,
            @RequestBody CategoryCreateRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CategoryResponse response = categoryService.createCategory(boardId, req.getName(), userDetails.getMember());
        return getResponseEntity("카테고리 생성 완료", response);
    }

    /**
     * 카테고리 전체 조회
     */
    @GetMapping("/{boardId}/categories")
    public ResponseEntity<CommonResponse<List<CategoryResponse>>> getAllCategories(
            @PathVariable Long boardId
    ){
        List<CategoryResponse> response = categoryService.getAllCategories(boardId);
        return getResponseEntity("모든 카테고리 조회 완료", response);
    }

    /**
     * 카테고리 수정
     */
    @PatchMapping("/{boardId}/categories/{categoryId}")
    public ResponseEntity<CommonResponse<CategoryResponse>> updateCategory(
            @PathVariable Long boardId,
            @PathVariable Long categoryId,
            @RequestBody CategoryUpdateRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CategoryResponse response = categoryService.updateCategory(boardId, categoryId, req, userDetails.getMember());
        return getResponseEntity("카테고리 수정 완료", response);
    }

    /**
     * 카테고리 순서 수정
     */
    @PatchMapping("/{boardId}/categories/{categoryId}/update-order")
    public ResponseEntity<CommonResponse<CategoryResponse>> updateOrderNumberCategory(
            @PathVariable Long boardId,
            @PathVariable Long categoryId,
            @RequestBody CategoryUpdateOrderRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CategoryResponse response = categoryService.updateOrderNumberCategory(boardId, categoryId, req, userDetails.getMember());
        return getResponseEntity("카테고리 수정 완료", response);
    }

    /**
     * 카테고리 삭제
     */
    @DeleteMapping("/{boardId}/categories/{categoryId}")
    public ResponseEntity<CommonResponse<Long>> deleteCategory(
            @PathVariable Long boardId,
            @PathVariable Long categoryId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        categoryService.deleteCategory(boardId, categoryId, userDetails.getMember());
        return getResponseEntity("카테고리 삭제 완료", categoryId);
    }
}
