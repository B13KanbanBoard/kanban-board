package com.sparta.kanbanboard.domain.category.service;


import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardRepository;
import com.sparta.kanbanboard.domain.category.dto.CategoryCreateResponse;
import com.sparta.kanbanboard.domain.category.dto.CategoryGetResponse;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.repository.CategoryRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
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
    public List<CategoryGetResponse> getAllCategories(Long boardId){
        Board tempBoard = boardRepository.findById(boardId).orElseThrow(NullPointerException::new);
        // 예외처리 nullpointer -> boardnotfound으로 변경시켜주기
        List<Category> categories = tempBoard.getCategoryList();
        List<CategoryGetResponse> result = new ArrayList<>();
        for (Category category : categories) {
            CategoryGetResponse res = new CategoryGetResponse(category.getId(), category.getName(), category.getOrderNumber());
            result.add(res);
        }
        return result;
    }

    /**
     * 특정 카테고리 조회
     */
    @Transactional(readOnly = true)
    public CategoryGetResponse getCategory(Long boardId, Long categoryId){
        checkBoardAndCategoryRelation(boardId, categoryId);
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(NullPointerException::new);
        // 예외처리 만들기
        return new CategoryGetResponse(tempCategory.getId(), tempCategory.getName(), tempCategory.getOrderNumber());
    }


    /**
     * 카테고리가 해당보드에 연관된것이 맞는지 확인
     */
    protected void checkBoardAndCategoryRelation(Long boardId, Long categoryId) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(NullPointerException::new);
        if(!Objects.equals(tempCategory.getBoard().getId(), boardId)){
            throw new IllegalArgumentException("Category does not belong to the board");
        }
    }


}
