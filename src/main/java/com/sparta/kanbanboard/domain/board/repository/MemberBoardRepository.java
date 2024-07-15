package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.domain.board.entity.BoardRole;
import com.sparta.kanbanboard.domain.board.entity.MemberBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBoardRepository extends JpaRepository<MemberBoard, Long> {

    Optional<MemberBoard> findByBoardIdAndMemberId(Long boardId, Long memberId);

    List<MemberBoard> findByMemberIdAndRole(Long id, BoardRole boardRole);

    boolean existsByBoardIdAndMemberId(Long boardId, Long memberId);

}
