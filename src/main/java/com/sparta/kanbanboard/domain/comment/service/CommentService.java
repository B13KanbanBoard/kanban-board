package com.sparta.kanbanboard.domain.comment.service;

import com.sparta.kanbanboard.common.exception.customexception.BoardNotFoundException;
import com.sparta.kanbanboard.common.exception.customexception.CommentMismatchException;
import com.sparta.kanbanboard.domain.board.entity.MemberBoard;
import com.sparta.kanbanboard.domain.board.repository.MemberBoardRepository;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardRepository;
import com.sparta.kanbanboard.domain.comment.dto.CommentRequest;
import com.sparta.kanbanboard.domain.comment.dto.CommentResponse;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.domain.comment.repository.CommentRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
import com.sparta.kanbanboard.domain.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.kanbanboard.common.exception.errorCode.BoardErrorCode.NOT_FOUND_BOARD;
import static com.sparta.kanbanboard.common.exception.errorCode.CommentErrorCode.NOT_MATH_CARD_ID;
import static com.sparta.kanbanboard.common.exception.errorCode.CommentErrorCode.NOT_OWNER_COMMENT;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final MemberBoardRepository memberBoardRepository;

    /**
     * 댓글 생성
     */
    @Transactional
    public CommentResponse createComment(long cardId, CommentRequest request, Member member) {
        Card card = getCard(cardId);

        long boardId = card.getCategory().getBoard().getId();

        // ADMIN이 아닌 경우
        if(member.getRole() != MemberRole.ADMIN) {
            MemberBoard memberBoard = memberBoardRepository.findByBoardIdAndMemberId(boardId, member.getId()).orElseThrow(
                    () -> new BoardNotFoundException(NOT_FOUND_BOARD)
            );
            //해당 보드의 소유 또는 초대한 사람인지 확인
        }

        Comment commentBuilder = Comment.builder().content(request.getContent())
                                            .card(card)
                                            .member(member)
                                            .build();
        Comment savedComment = commentRepository.save(commentBuilder);

        return CommentResponse.builder()
                .id(savedComment.getId())
                .build();
    }

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getComment(long cardId) {
        Card card = getCard(cardId);

        List<Comment> commentList = commentRepository.findAllByCardIdOrderByCreatedAtDesc(card.getId());

        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseList.add(new CommentResponse(comment));
        }

        return commentResponseList;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponse updateComment(long cardId, long commentId, CommentRequest request, Member member) {
        Card card = getCard(cardId);

        Comment comment = commentRepository.findByIdOrThrow(commentId);
        // ADMIN이 아닌 경우
        if(member.getRole() != MemberRole.ADMIN) {
            // 자신이 쓴 댓글인지 확인
            if(comment.getMember().getId() != member.getId()) {
                throw new CommentMismatchException(NOT_OWNER_COMMENT);
            }
        }

        //요청한 URL의 cardId가 실제 댓글의 상위 cardId가 다를 경우
        if(comment.getCard().getId() != card.getId()) {
            throw new CommentMismatchException(NOT_MATH_CARD_ID);
        }

        comment.update(request.getContent());

        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponse(updatedComment);
    }

    /**
     * 댓글 삭제
     */
    public CommentResponse deleteComment(long cardId, long commentId, Member member) {
        Card card = getCard(cardId);

        Comment comment = commentRepository.findByIdOrThrow(commentId);
        // ADMIN이 아닌 경우
        if(member.getRole() != MemberRole.ADMIN) {
            // 자신이 쓴 댓글인지 확인
            if(comment.getMember().getId() != member.getId()) {
                throw new CommentMismatchException(NOT_OWNER_COMMENT);
            }
        }

        //요청한 URL의 cardId가 실제 댓글의 상위 cardId가 다를 경우
        if(comment.getCard().getId() != card.getId()) {
            throw new CommentMismatchException(NOT_MATH_CARD_ID);
        }

        commentRepository.delete(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .build();
    }

    private Card getCard(long id){
        Card card = cardRepository.findByIdOrThrow(id);

        return card;
    }
}
