package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.domain.board.dto.BoardDetailResponse;
import com.sparta.kanbanboard.domain.board.entity.Board;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> searchMyBoards(Long memberId, Pageable pageable);
    List<BoardDetailResponse> searchBoardDetails(Long boardId);
}
