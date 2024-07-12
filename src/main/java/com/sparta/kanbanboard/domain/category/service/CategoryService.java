package com.sparta.kanbanboard.domain.category.service;


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
//        Board tempBoard = boardRepository.findById(boardId).orElseThrow(NullPointerException::new);
//        // 예외처리 수정
//        if( (!Objects.equals(tempBoard.getMember().getId(), member.getId())) && (!member.getRole().equals(MemberRole.ADMIN)) ){
//            throw new IllegalArgumentException("해당 멤버는 카테고리에 작업 권한이 없습니다");
//        }

        Board tempBoard = boardRepository.findById(boardId).orElseThrow(NullPointerException::new);
        // 예외처리 nullpointer -> boardnotfound으로 변경시켜주기
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
        Board tempBoard = boardRepository.findById(boardId).orElseThrow(NullPointerException::new);
        // 예외처리 nullpointer -> boardnotfound으로 변경시켜주기
        List<Category> categories = tempBoard.getCategoryList();
        List<CategoryResponse> result = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse res = new CategoryResponse(category.getId(), category.getName(), category.getOrderNumber());
            result.add(res);
        }
        return result;
    }

    /**
     * 특정 카테고리 조회
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long boardId, Long categoryId){
        checkBoardAndCategoryRelation(boardId, categoryId);
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(NullPointerException::new);
        // 예외처리 만들기
        return new CategoryResponse(tempCategory.getId(), tempCategory.getName(), tempCategory.getOrderNumber());
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public CategoryResponse updateCategory(Long boardId, Long categoryId, CategoryUpdateRequest request, Member member) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(NullPointerException::new);
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
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(NullPointerException::new);
        checkMemberAuthToCategory(member, tempCategory);
        checkBoardAndCategoryRelation(boardId, categoryId);

        categoryRepository.delete(tempCategory);
    }

    /**
     * 카테고리가 해당보드에 연관된것이 맞는지 확인
     */
    public void checkBoardAndCategoryRelation(Long boardId, Long categoryId) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(NullPointerException::new);
        if(!Objects.equals(tempCategory.getBoard().getId(), boardId)){
            throw new IllegalArgumentException("Category does not belong to the board");
        }
    }

    /**
     * 카테고리가 해당 유저가 만든것인지 확인
     */
    public void checkMemberAuthToCategory(Member member, Category category) {
        if( (!Objects.equals(category.getMember().getId(), member.getId())) && (!member.getRole().equals(MemberRole.ADMIN)) ){
            // 멤버롤 대신 멤버보드에서 롤이 생성자가 맞는지 확인하는 로직으로 변경 필요
            throw new IllegalArgumentException("해당 멤버는 카테고리에 작업 권한이 없습니다");
        }
    }
}