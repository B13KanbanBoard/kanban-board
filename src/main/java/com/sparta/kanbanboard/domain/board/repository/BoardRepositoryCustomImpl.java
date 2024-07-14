package com.sparta.kanbanboard.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.kanbanboard.domain.board.dto.BoardDetailResponse;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.entity.QBoard;
import com.sparta.kanbanboard.domain.board.entity.QMemberBoard;
import com.sparta.kanbanboard.domain.card.entity.QCard;
import com.sparta.kanbanboard.domain.category.entity.QCategory;
import com.sparta.kanbanboard.domain.member.entity.QMember;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Board> searchMyBoards (Long memberId, Pageable pageable) {

        QMember member = QMember.member;
        QMemberBoard memberBoard = QMemberBoard.memberBoard;
        QBoard board = QBoard.board;

        List<Board> boardList = jpaQueryFactory
                .selectFrom(board)
                .leftJoin(memberBoard).on(memberBoard.board.id.eq(board.id))
                .leftJoin(member).on(member.id.eq(memberBoard.member.id))
                .where(member.id.eq(memberId))
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(board.count())
                .from(board)
                .leftJoin(memberBoard).on(memberBoard.board.id.eq(board.id))
                .leftJoin(member).on(member.id.eq(memberBoard.member.id))
                .where(member.id.eq(memberId))
                .fetch().get(0);

        return PageableExecutionUtils.getPage(boardList, pageable, () -> total);
    }

    @Override
    public List<BoardDetailResponse> searchBoardDetails (Long boardId) {

        QBoard board = QBoard.board;
        QCategory category = QCategory.category;
        QCard card = QCard.card;

        return jpaQueryFactory
                .select(Projections.constructor(BoardDetailResponse.class,
                        board.id,
                        board.boardName,
                        board.content,
                        category.id,
                        category.name,
                        category.orderNumber,
                        card.id,
                        card.title,
                        card.orderNumber
                ))
                .from(board)
                .leftJoin(category).on(category.board.id.eq(board.id))
                .leftJoin(card).on(card.category.id.eq(category.id))
                .where(board.id.eq(boardId))
                .fetch();
    }

}
