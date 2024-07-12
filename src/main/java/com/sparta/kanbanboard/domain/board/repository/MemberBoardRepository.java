package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.entity.MemberBoard;
import com.sparta.kanbanboard.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBoardRepository extends JpaRepository<MemberBoard, Long> {

    // 특정 멤버와 보드에 해당하는 MemberBoard 엔티티를 찾는 메서드
    Optional<MemberBoard> findByMemberIdAndBoardId(Long memberId, Long boardId);

//    Optional<MemberBoard> findByMemberIdAndBoardId(Long BoardId, Long mebmerId);

    Optional<MemberBoard> findByMemberAndBoard(Member member, Board board);

    // 특정 보드에 해당하는 모든 MemberBoard 엔티티를 찾는 메서드
    List<MemberBoard> findByBoardId(Board board);

}
