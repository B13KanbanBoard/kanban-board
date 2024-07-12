package com.sparta.kanbanboard.domain.category.service;


import com.sparta.kanbanboard.common.exception.customexception.BoardNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.CategoryNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.MemberAccessDeniedException;
import com.sparta.kanbanboard.common.exception.customexception.PathMismatchException;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardRepository;
import com.sparta.kanbanboard.domain.category.dto.CategoryCreateResponse;
import com.sparta.kanbanboard.domain.category.dto.CategoryResponse;
import com.sparta.kanbanboard.domain.category.dto.CategoryUpdateRequest;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.repository.CategoryRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode.*;

@Service
@Slf4j(topic = "CategoryService")
@RequiredArgsConstructor
public class CategoryService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 생성
     */
    @Transactional
    public CategoryCreateResponse createCategory(Long boardId, String name, Member member) {
        // 멤버가 매니저 역할인지 확인하는 메서드 추가하기

        Board tempBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(BOARD_NOT_FOUND));
        Long orderNum = (long) (tempBoard.getCategoryList().size() + 1);

        Category category = new Category(name, orderNum);
        category.setMember(member);
        category.setBoard(tempBoard);

        categoryRepository.save(category);

        return new CategoryCreateResponse(category.getId(), category.getName(), category.getOrderNumber());
    }

    /**
     * 전체 카테고리 조회
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(Long boardId){
        return categoryRepository.getCategoryListSortOrderNumber(boardId)
                .stream()
                .map(m ->
                        CategoryResponse.builder()
                                .id(m.getId())
                                .name(m.getName())
                                .orderNumber(m.getOrderNumber())
                                .build()
                ).collect(Collectors.toList());
    }

    /**
     * 특정 카테고리 조회
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long boardId, Long categoryId){
        checkBoardAndCategoryRelation(boardId, categoryId);
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        return new CategoryResponse(tempCategory.getId(), tempCategory.getName(), tempCategory.getOrderNumber());
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public CategoryResponse updateCategory(Long boardId, Long categoryId, CategoryUpdateRequest request, Member member) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));
        checkMemberAuthToCategory(member, tempCategory);
        checkBoardAndCategoryRelation(boardId, categoryId);


        String newName = request.getName();
        Long newOrderNumber = request.getOrderNumber();

        if(newName != null){
            tempCategory.updateName(newName);
        }
        if(newOrderNumber != null){
            // newOrder가 이미 존재하는 순서인지 확인하는 로직 필요
            // 순서 바꾸는거에 대한 로직 생각이 더 필요
            tempCategory.updateOrderNumber(newOrderNumber);
        }

        return new CategoryResponse(tempCategory.getId(), tempCategory.getName(), tempCategory.getOrderNumber());
    }

    /**
     * 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long boardId, Long categoryId, Member member) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));
        checkMemberAuthToCategory(member, tempCategory);
        checkBoardAndCategoryRelation(boardId, categoryId);

        categoryRepository.delete(tempCategory);
    }

    /**
     * 카테고리가 해당보드에 연관된것이 맞는지 확인
     */
    public void checkBoardAndCategoryRelation(Long boardId, Long categoryId) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));
        if(!Objects.equals(tempCategory.getBoard().getId(), boardId)){
            throw new PathMismatchException(BAD_REQUEST);
        }
    }

    /**
     * 카테고리가 해당 유저가 만든것인지 확인
     */
    public void checkMemberAuthToCategory(Member member, Category category) {
        if( (!Objects.equals(category.getMember().getId(), member.getId())) && (!member.getRole().equals(MemberRole.ADMIN)) ){
            // 멤버롤 대신 멤버보드에서 롤이 생성자가 맞는지 확인하는 로직으로 변경 필요
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }
    }
}
