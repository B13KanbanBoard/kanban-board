package com.sparta.kanbanboard.domain.board.service;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.exception.customexception.BoardNotFoundException;
import com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode;
import com.sparta.kanbanboard.domain.board.dto.BoardRequest;
import com.sparta.kanbanboard.domain.board.dto.BoardResponse;
import com.sparta.kanbanboard.domain.board.dto.MemberBoardRequest;
import com.sparta.kanbanboard.domain.board.dto.MemberBoardResponse;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.entity.BoardRole;
import com.sparta.kanbanboard.domain.board.entity.MemberBoard;
import com.sparta.kanbanboard.domain.board.repository.BoardRepository;
import com.sparta.kanbanboard.domain.board.repository.MemberBoardRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private MemberRepository memberRepository;

    @Transactional
    public BoardResponse createBoard(BoardRequest request, Member member) {

        Board board = new Board(member, request.getBoardName(), request.getContent());
        MemberBoard memberBoard = new MemberBoard(board,member,BoardRole.MANAGER);
        board.addMemberBoard(memberBoard);
        Board savedBoard = boardRepository.save(board);
        return new BoardResponse(savedBoard);
    }

    @Transactional(readOnly = true)
    public Page<BoardResponse> getBoards(Long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
        Page<Board> boardPage = boardRepository.findById(boardId, pageable);
        return boardPage.map(BoardResponse::new);
    }

    @Transactional
    public CommonResponse<BoardResponse> updateBoard(Long boardId, BoardRequest request, Member member) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.NOT_FOUND_BOARD));

        board.update(request.getBoardName(), request.getContent());
        BoardResponse response = new BoardResponse(board);
        return new CommonResponse<>(true,200,"보드 수정이 성공적으로 되었습니다.",response);
    }

    @Transactional
    public CommonResponse<Long> deleteBoard(Long boardId, Long id) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.NOT_FOUND_BOARD));

        boardRepository.delete(board);
        return new CommonResponse<>(true, 200, "보드 삭제가 성공적으로 되었습니다.", boardId);
    }

    @Transactional
    public List<MemberBoardResponse> inviteMemberBoard(MemberBoardRequest request, Member currentUser)
        throws Exception {
        Long boardId = request.getBoardId();
        List<Long> memberIds = request.getMemberIdList();

        // 현재 사용자가 필요한 권한을 가지고 있는지 확인
        Optional<MemberBoard> currentUserRole = memberBoardRepository.findByMemberAndBoard(currentUser, boardRepository.findById(boardId).orElseThrow());
        if(!currentUserRole.isPresent() || !currentUserRole.get().getRole().equals(BoardRole.MANAGER)) {
            throw new Exception("해당 보드에 사용자를 초대할 권한이 없습니다.");
        }

        Board board = boardRepository.findById(boardId).orElseThrow(()-> new BoardNotFoundException(BoardErrorCode.NOT_FOUND_BOARD));

        List<MemberBoardResponse> invitedMembers = new ArrayList<>();

        for(Long memberId : memberIds) {
            // 사용자가 존재하는지 확인
            Member member = memberRepository.findById(memberId).orElseThrow(()-> new Exception("ID" + memberId + "인 사용자가 존재하지 않습니다"));

            // 사용자가 이미 보드에 초대되었는지 확인
            Optional<MemberBoard> existingMemberBoard = memberBoardRepository.findByMemberAndBoard(currentUser,  boardRepository.findById(boardId).orElseThrow());
            if (existingMemberBoard.isPresent()) {
                throw new Exception("ID " + memberId + "인 사용자가 이미 보드에 초대되었습니다.");
            }

            // 생성자를 사용하여 멤버를 기본 역할로 보드에 추가
            MemberBoard memberBoard = new MemberBoard(board, member, BoardRole.PARTICIPANTS);
            memberBoardRepository.save(memberBoard);

            // 초대된 사용자 정보를 MemberBoardResponse로 변환하여 리스트에 추가
            invitedMembers.add(new MemberBoardResponse(member, board, BoardRole.PARTICIPANTS));
        }
        return invitedMembers;
    }
}
