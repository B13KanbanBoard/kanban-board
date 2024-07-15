package com.sparta.kanbanboard.domain.category.service;


import com.sparta.kanbanboard.common.exception.customexception.BoardNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.CategoryNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.MemberAccessDeniedException;
import com.sparta.kanbanboard.common.exception.customexception.PathMismatchException;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardRepository;
import com.sparta.kanbanboard.domain.category.dto.CategoryResponse;
import com.sparta.kanbanboard.domain.category.dto.CategoryUpdateOrderRequest;
import com.sparta.kanbanboard.domain.category.dto.CategoryUpdateRequest;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.repository.CategoryRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CategoryResponse createCategory(Long boardId, String name, Member member) {
        // 멤버가 매니저 역할인지 확인하는 메서드 추가하기

        Board tempBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(BOARD_NOT_FOUND));
        Long orderNum = (long) (tempBoard.getCategoryList().size() + 1);

        Category category = new Category(name, orderNum, member, tempBoard);

        // 이름 중복 확인
        category.checkCategoryNameDuplicate(name);

        categoryRepository.save(category);

        return new CategoryResponse(category.getId(), category.getName(), category.getOrderNumber());
    }

    /**
     * 전체 카테고리 조회
     */
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
     * 카테고리 수정
     */
    @Transactional
    public CategoryResponse updateCategory(Long boardId, Long categoryId, CategoryUpdateRequest request, Member member) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));
        checkMemberAuthToCategory(member, tempCategory);
        tempCategory.checkBoardAndCategoryRelation(boardId);

        String newName = request.getName();
        tempCategory.updateName(newName);

        return new CategoryResponse(tempCategory.getId(), tempCategory.getName(), tempCategory.getOrderNumber());
    }

    /**
     * order number 수정
     */
    @Transactional
    public CategoryResponse updateOrderNumberCategory(Long boardId, Long categoryId, CategoryUpdateOrderRequest request, Member member) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));
        checkMemberAuthToCategory(member, tempCategory);
        tempCategory.checkBoardAndCategoryRelation(boardId);

        Long newOrderNumber = request.getOrderNumber();

        tempCategory.updateOrderNumber(newOrderNumber);

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
        tempCategory.checkBoardAndCategoryRelation(boardId);

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
