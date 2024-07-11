package com.sparta.kanbanboard.domain.category.service;


import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardRepository;
import com.sparta.kanbanboard.domain.category.dto.CategoryCreateResponse;
import com.sparta.kanbanboard.domain.category.entity.Category;
import com.sparta.kanbanboard.domain.category.repository.CategoryRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Board tempBoard = boardRepository.findById(boardId).orElseThrow(NullPointerException::new);
        Long orderNum = (long) (tempBoard.getCategoryList().size() + 1);

        Category category = new Category(name, orderNum);
        // 예외처리 nullpointer -> boardnotfound으로 변경시켜주기

        category.setBoard(tempBoard);
        category.setMember(member);

        categoryRepository.save(category);

        return new CategoryCreateResponse(category.getId(), category.getName(), category.getOrderNumber());
    }


}
