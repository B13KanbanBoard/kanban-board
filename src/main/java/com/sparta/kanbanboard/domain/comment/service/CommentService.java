package com.sparta.kanbanboard.domain.comment.service;

import com.sparta.kanbanboard.common.exception.customexception.CommentMismatchException;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.comment.dto.CommentRequest;
import com.sparta.kanbanboard.domain.comment.dto.CommentResponse;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.domain.comment.repository.CommentRepository;
import com.sparta.kanbanboard.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.kanbanboard.common.exception.errorCode.CommentErrorCode.NOT_OWNER_COMMENT;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    //private final CardRepository cardRepository;

    @Transactional
    public CommentResponse createComment(long cardId, CommentRequest request, Member member) {
        Card card = getCard(cardId);

        Comment commentBuilder = Comment.builder().content(request.getContent())
                                            .card(card)
                                            .member(member)
                                            .build();
        Comment savedComment = commentRepository.save(commentBuilder);

        return CommentResponse.builder()
                .id(savedComment.getId())
                .build();
    }

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

    @Transactional
    public CommentResponse updateComment(long cardId, long commentId, CommentRequest request, Member member) {
        Card card = getCard(cardId);

        Comment comment = commentRepository.findByIdOrThrow(commentId);
        if(comment.getMember().getId() != member.getId()) {
            throw new CommentMismatchException(NOT_OWNER_COMMENT);
        }

        comment.update(request.getContent());

        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponse(updatedComment);
    }

    public CommentResponse deleteComment(long cardId, long commentId, Member member) {
        Card card = getCard(cardId);

        Comment comment = commentRepository.findByIdOrThrow(commentId);
        if(comment.getMember().getId() != member.getId()) {
            throw new CommentMismatchException(NOT_OWNER_COMMENT);
        }

        commentRepository.delete(comment);

        return CommentResponse.builder()
                .id(comment.getId())
                .build();
    }

    private Card getCard(long id){
        //Card card = cardrepository.find...
        //Card Not Find exception 추가하기

        return null;
    }
}
