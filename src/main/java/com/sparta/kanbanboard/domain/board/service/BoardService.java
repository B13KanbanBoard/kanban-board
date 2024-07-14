package com.sparta.kanbanboard.domain.board.service;

import static com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode.CANNOT_DELETE_SELF;
import static com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode.CANNOT_INVITE_SELF;
import static com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode.INAPPROPRIATE_MEMBER_BOARD;
import static com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode.NOT_FOUND_BOARD;
import static com.sparta.kanbanboard.common.exception.errorCode.MemberErrorCode.NOT_FOUND_USER;

import com.sparta.kanbanboard.common.exception.customexception.BoardInappropriateException;
import com.sparta.kanbanboard.common.exception.customexception.BoardNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.MemberNotFoundException;
import com.sparta.kanbanboard.domain.board.dto.BoardDetailResponse;
import com.sparta.kanbanboard.domain.board.dto.BoardRequest;
import com.sparta.kanbanboard.domain.board.dto.BoardResponse;
import com.sparta.kanbanboard.domain.board.dto.InviteMemberRequest;
import com.sparta.kanbanboard.domain.board.dto.InviteMemberResponse;
import com.sparta.kanbanboard.domain.board.dto.PageBoardResponse;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.entity.BoardRole;
import com.sparta.kanbanboard.domain.board.entity.MemberBoard;
import com.sparta.kanbanboard.domain.board.repository.BoardRepository;
import com.sparta.kanbanboard.domain.board.repository.MemberBoardRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberBoardRepository memberBoardRepository;
    private final MemberRepository memberRepository;

    /**
     * Board 생성
     */
    @Transactional
    public BoardResponse createBoard (BoardRequest request, Member loginMember) {
        Board board = Board.createBoard(request.getBoardName(), request.getContent());

        MemberBoard memberBoard = MemberBoard.createMemberBoard(loginMember, board, BoardRole.MANAGER);
        board.addMemberBoard(memberBoard);

        boardRepository.save(board);

        return BoardResponse.of(board);
    }

    /**
     * Board page 조회
     */
    @Transactional(readOnly = true)
    public PageBoardResponse getBoards (Integer pageNum, Integer size, Member loginMember) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Direction.DESC, "createdAt"));
        Page<Board> boardPage = boardRepository.searchMyBoards(loginMember.getId(), pageable);

        return PageBoardResponse.of(pageNum, boardPage.getTotalElements(), boardPage);
    }

    /**
     * Board 상세 조회
     */
    @Transactional(readOnly = true)
    public List<BoardDetailResponse> getBoardDetails (Long boardId) {
        return boardRepository.searchBoardDetails(boardId);
    }

    /**
     * Board 수정
     */
    @Transactional
    public BoardResponse updateBoard (Long boardId, BoardRequest request, Member loginMember) {
        Board board = findById(boardId);
        checkMemberAboutBoard(boardId, loginMember.getId());

        board.updateBoard(request.getBoardName(), request.getContent());

        return BoardResponse.of(board);
    }

    /**
     * Board 삭제
     */
    @Transactional
    public String deleteBoard (Long boardId, Member loginMember) {
        Board board = findById(boardId);
        checkMemberAboutBoard(boardId, loginMember.getId());

        boardRepository.delete(board);

        return board.getBoardName();
    }

    /**
     * Board에 member 초대
     */
    @Transactional
    public InviteMemberResponse inviteMember (InviteMemberRequest request, Member loginMember) {
        Board board = findById(request.getBoardId());
        checkMemberAboutBoard(request.getBoardId(), loginMember.getId());

        boolean isSelfInvite = request.getMemberIdList().stream()
                        .anyMatch(inviteMemberId -> inviteMemberId.equals(loginMember.getId()));

        if (isSelfInvite) {
            throw new BoardInappropriateException(CANNOT_INVITE_SELF);
        }

        List<Member> inviteMemberList = memberRepository.findByIdIn(request.getMemberIdList());
        if(inviteMemberList.isEmpty()){
            throw new MemberNotFoundException(NOT_FOUND_USER);
        }

        inviteMemberList.stream()
                .map(m -> MemberBoard.createMemberBoard(m, board, BoardRole.PARTICIPANTS))
                .forEach(board::addMemberBoard);

        return InviteMemberResponse.of(board.getId(), inviteMemberList);
    }

    /**
     * Board에 멤버 삭제
     */
    @Transactional
    public String deleteBoardMember(Long boardId, Long memberId, Member loginMember) {
        Board board = findById(boardId);
        checkMemberAboutBoard(boardId, loginMember.getId());

        MemberBoard memberBoard = memberBoardRepository.findByBoardIdAndMemberId(boardId, memberId)
                .orElseThrow(
                        () -> new BoardNotFoundException(NOT_FOUND_BOARD)
                );

        if(loginMember.getId().equals(memberBoard.getMember().getId())){
            throw new BoardInappropriateException(CANNOT_DELETE_SELF);
        }

        board.getMemberBoardList().remove(memberBoard);

        return memberBoard.getMember().getEmail();
    }

    /**
     * id로 Board 조회
     */
    private Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(NOT_FOUND_BOARD)
        );
    }

    /**
     * MemberBoard 존재 확인 및 권한 확인
     */
    private void checkMemberAboutBoard(Long boardId, Long memberId) {
        MemberBoard memberBoard = memberBoardRepository.findByBoardIdAndMemberId(boardId, memberId).orElseThrow(
                () -> new BoardNotFoundException(NOT_FOUND_BOARD)
        );

        if(BoardRole.PARTICIPANTS.equals(memberBoard.getRole())){
            throw new BoardInappropriateException(INAPPROPRIATE_MEMBER_BOARD);
        }
    }

}
