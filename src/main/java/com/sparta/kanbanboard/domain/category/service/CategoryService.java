package com.sparta.kanbanboard.domain.category.service;


import com.sparta.kanbanboard.common.exception.customexception.BoardNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.CategoryNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.MemberAccessDeniedException;
import com.sparta.kanbanboard.common.exception.customexception.PathMismatchException;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.entity.MemberBoard;
import com.sparta.kanbanboard.domain.board.repository.BoardRepository;
import com.sparta.kanbanboard.domain.board.repository.MemberBoardRepository;
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

import static com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode.NOT_FOUND_BOARD;
import static com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode.*;

@Service
@Slf4j(topic = "CategoryService")
@RequiredArgsConstructor
public class CategoryService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final MemberBoardRepository memberBoardRepository;

    /**
     * 카테고리 생성
     */
    @Transactional
    public CategoryResponse createCategory(Long boardId, String name, Member member) {
        // 보드 참여자인지 확인
        if(!memberBoardRepository.existsByBoardIdAndMemberId(boardId, member.getId())){
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }

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
    public List<CategoryResponse> getAllCategories(Long boardId, Member member){
        // 보드 참여자인지 확인
        if(!memberBoardRepository.existsByBoardIdAndMemberId(boardId, member.getId())){
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }

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
        // 유저 권한 확인
        checkMemberAuthToCategory(boardId, member.getId(), tempCategory);

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
        // 유저 권한 확인
        checkMemberAuthToCategory(boardId, member.getId(), tempCategory);
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
        // 유저 권한 확인
        checkMemberAuthToCategory(boardId, member.getId(), tempCategory);

        tempCategory.checkBoardAndCategoryRelation(boardId);

        categoryRepository.delete(tempCategory);
    }


    /**
     * 보드 매니저 or 카테고리 생성자 인지 확인
     */
    public void checkMemberAuthToCategory(Long boardId, Long memberId, Category category) {
        MemberBoard memberBoard = memberBoardRepository.findByBoardIdAndMemberId(boardId, memberId).orElseThrow(
                () -> new BoardNotFoundException(NOT_FOUND_BOARD)
        );
        if( (!Objects.equals(category.getMember().getId(), memberId)) && (!memberBoard.checkIfManager()) ){
            throw new MemberAccessDeniedException(AUTH_USER_FORBIDDEN);
        }
    }

}
