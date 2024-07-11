package com.sparta.kanbanboard.domain.category.controller;


import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.category.dto.CategoryCreateRequest;
import com.sparta.kanbanboard.domain.category.dto.CategoryCreateResponse;
import com.sparta.kanbanboard.domain.category.dto.CategoryGetResponse;
import com.sparta.kanbanboard.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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
    public ResponseEntity<CommonResponse<CategoryCreateResponse>> createCategory(
            @PathVariable Long boardId,
            @RequestBody CategoryCreateRequest req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CategoryCreateResponse response = categoryService.createCategory(boardId, req.getName(), userDetails.getMember());
        return getResponseEntity("카테고리 생성 완료", response);
    }

    /**
     * 카테고리 전체 조회
     */
    @GetMapping("/{boardId}/categories")
    public ResponseEntity<CommonResponse<List<CategoryGetResponse>>> getAllCategories(
            @PathVariable Long boardId
    ){
        List<CategoryGetResponse> response = categoryService.getAllCategories(boardId);
        return getResponseEntity("모든 카테고리 조회 완료", response);
    }

    /**
     * 특정카테고리 조회
     */
    @GetMapping("/{boardId}/categories/{categoryId}")
    public ResponseEntity<CommonResponse<CategoryGetResponse>> getCategory(
            @PathVariable Long boardId,
            @PathVariable Long categoryId
    ){
        CategoryGetResponse response = categoryService.getCategory(boardId, categoryId);
        return getResponseEntity("해당 카테고리 조회 완료", response);
    }
}
