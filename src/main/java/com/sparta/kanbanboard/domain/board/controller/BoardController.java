package com.sparta.kanbanboard.domain.board.controller;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.domain.board.dto.BoardRequest;
import com.sparta.kanbanboard.domain.board.dto.BoardResponse;
import com.sparta.kanbanboard.domain.board.dto.MemberBoardRequest;
import com.sparta.kanbanboard.domain.board.dto.MemberBoardResponse;
import com.sparta.kanbanboard.domain.board.dto.PageableResponse;
import com.sparta.kanbanboard.domain.board.service.BoardService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 보드 생성
    @PostMapping
    public ResponseEntity<CommonResponse<BoardResponse>> createBoard(
        @RequestBody BoardRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponse response = boardService.createBoard(request,userDetails.getMember());
        CommonResponse<BoardResponse> responseEntity = new CommonResponse<>(true,200,"보드가 성공적으로 생성되었습니다.", response);
        return ResponseEntity.ok().body(responseEntity);
    }

    //보드 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<PageableResponse<BoardResponse>> getBoard(
        @PathVariable Long boardId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size){
        Page<BoardResponse> response = boardService.getBoards(boardId, page, size);
        PageableResponse<BoardResponse> responseEntity = new PageableResponse<>(response);
        return ResponseEntity.ok().body(responseEntity);
    }

    //보드 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<CommonResponse<BoardResponse>> updateBoard (
        @PathVariable Long boardId,
        @RequestBody BoardRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponse response = boardService.updateBoard(boardId, request, userDetails.getMember())
            .getData();
        CommonResponse<BoardResponse> responseEntity = new CommonResponse<>(true,200,"보드가 성공적으로 수정되었습니다.",response);
        return ResponseEntity.ok().body(responseEntity);
    }

    //보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResponse<Long>> deleteBoard(
        @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommonResponse<Long> responseEntity = boardService.deleteBoard(boardId, userDetails.getMember().getId());
        return ResponseEntity.ok().body(responseEntity);
    }

    //보드 사용자 초대
    @PostMapping("/members")
    public ResponseEntity<CommonResponse<List<MemberBoardResponse>>> inviteMember(
        @RequestBody MemberBoardRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<MemberBoardResponse> response = boardService.inviteMemberBoard(request, userDetails.getMember());
            CommonResponse<List<MemberBoardResponse>> responseEntity = new CommonResponse<>(true, 200, "사용자가 성공적으로 초대되었습니다", response);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            CommonResponse<List<MemberBoardResponse>> responseEntity = new CommonResponse<>(false, 400, e.getMessage(), null);
            return ResponseEntity.status(400).body(responseEntity);
        }
    }

}
