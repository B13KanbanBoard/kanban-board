package com.sparta.kanbanboard.domain.board.dto;

import com.sparta.kanbanboard.domain.board.entity.Board;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
public record PageBoardResponse(Integer currentPage, Long totalCount,
                                List<BoardResponse> boardResponseList) {

    public static PageBoardResponse of(Integer currentPage, Long totalCount, Page<Board> boards) {
        return PageBoardResponse.builder()
                .currentPage(currentPage)
                .totalCount(totalCount)
                .boardResponseList(boards.getContent().stream().map(BoardResponse::of).toList())
                .build();
    }
}
