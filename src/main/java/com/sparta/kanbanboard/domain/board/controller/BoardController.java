package com.sparta.kanbanboard.domain.board.controller;

import static com.sparta.kanbanboard.common.util.ControllerUtil.getResponseEntity;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.board.dto.BoardDetailResponse;
import com.sparta.kanbanboard.domain.board.dto.BoardRequest;
import com.sparta.kanbanboard.domain.board.dto.BoardResponse;
import com.sparta.kanbanboard.domain.board.dto.InviteMemberRequest;
import com.sparta.kanbanboard.domain.board.dto.InviteMemberResponse;
import com.sparta.kanbanboard.domain.board.dto.PageBoardResponse;
import com.sparta.kanbanboard.domain.board.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * Board 생성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<BoardResponse>> createBoard (
            @Valid @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponse response = boardService.createBoard(request, userDetails.getMember());
        return getResponseEntity("보드 생성 추가", response);
    }

    /**
     * Board page 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<PageBoardResponse>> getBoards (
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") final Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "4") final Integer size,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        PageBoardResponse response = boardService.getBoards(pageNum, size, userDetails.getMember());
        return getResponseEntity(pageNum + "번 page BoardList 조회 성공", response);
    }

    /**
     * Board 상세 조회
     */
    @GetMapping("/boardDetails")
    public ResponseEntity<CommonResponse<List<BoardDetailResponse>>> getBoardDetails (
            @RequestParam(value = "boardId") final Long boardId
    ) {
        List<BoardDetailResponse> response = boardService.getBoardDetails(boardId);
        return getResponseEntity("보드 상세 조회 성공", response);
    }

    /**
     * Board 수정
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<CommonResponse<BoardResponse>> updateBoard (
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponse response = boardService.updateBoard(boardId, request, userDetails.getMember());
        return getResponseEntity("보드 수정 성공", response);
    }

    /**
     * Board 삭제
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResponse<String>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String response = boardService.deleteBoard(boardId, userDetails.getMember());
        return getResponseEntity("Board: " + response + "삭제 성공", response);
    }

    /**
     * Board에 member 초대
     */
    @PostMapping("/invite-member")
    public ResponseEntity<CommonResponse<InviteMemberResponse>> inviteMember (
            @Valid @RequestBody InviteMemberRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        InviteMemberResponse response = boardService.inviteMember(request, userDetails.getMember());
        return getResponseEntity("초대 성공", response);
    }

    /**
     * Board에 멤버 삭제
     */
    @DeleteMapping("/{boardId}/{memberId}")
    public ResponseEntity<CommonResponse<String>> deleteBoardMember (
            @PathVariable("boardId") Long boardId,
            @PathVariable("memberId") Long memberId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String response = boardService.deleteBoardMember(boardId, memberId, userDetails.getMember());
        return getResponseEntity("유저 삭제 성공", response);
    }

}
