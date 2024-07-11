package com.sparta.kanbanboard.domain.category.controller;


import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.category.dto.CategoryCreateRequest;
import com.sparta.kanbanboard.domain.category.dto.CategoryCreateResponse;
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

        return getResponseEntity("success", response);
    }
}
