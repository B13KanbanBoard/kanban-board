package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findById(Long boardId, Pageable pageable);
    //optional이 들어가야함
}
