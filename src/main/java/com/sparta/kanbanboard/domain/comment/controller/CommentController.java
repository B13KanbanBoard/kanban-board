package com.sparta.kanbanboard.domain.comment.controller;

import com.sparta.kanbanboard.common.base.dto.CommonResponse;
import com.sparta.kanbanboard.common.security.UserDetailsImpl;
import com.sparta.kanbanboard.common.status.StatusCode;
import com.sparta.kanbanboard.domain.comment.dto.CommentRequest;
import com.sparta.kanbanboard.domain.comment.dto.CommentResponse;
import com.sparta.kanbanboard.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 생성
     */
    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity<CommonResponse<CommentResponse>> createComment(@PathVariable long cardId,
                                                                         @RequestBody CommentRequest request,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentResponse responseData = commentService.createComment(cardId,request,userDetails.getMember());

        return ResponseEntity.ok()
                .body(CommonResponse.of(StatusCode.CREATED.code,"Comment 생성 완료", responseData));
    }

    /**
     * 댓글 조회
     */
    @GetMapping("/cards/{cardId}/comments")
    public ResponseEntity<CommonResponse<List<CommentResponse>>> getComment(@PathVariable long cardId
                                                                            , @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<CommentResponse> responseData = commentService.getComment(cardId);

        return ResponseEntity.ok()
                .body(CommonResponse.of(StatusCode.OK.code,"Comment 조회", responseData));
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponse>> updateComment(@PathVariable long cardId, @PathVariable long commentId,
                                                                         @RequestBody CommentRequest request,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentResponse responseData = commentService.updateComment(cardId,commentId,request,userDetails.getMember());

        return ResponseEntity.ok()
                .body(CommonResponse.of(StatusCode.CREATED.code,"Comment 수정 완료", responseData));
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponse>> deleteComment(@PathVariable long cardId, @PathVariable long commentId,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentResponse responseData = commentService.deleteComment(cardId,commentId,userDetails.getMember());

        return ResponseEntity.ok()
                .body(CommonResponse.of(StatusCode.CREATED.code,"Comment 삭제 완료", responseData));
    }
}

