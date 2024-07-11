package com.sparta.kanbanboard.domain.comment.repository;

import com.sparta.kanbanboard.common.exception.customexception.CommentNotFoundException;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.sparta.kanbanboard.common.exception.errorCode.CommentErrorCode.NOT_FOUND_COMMENT;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCardIdOrderByCreatedAtDesc(long cardId);

    default Comment findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(()-> new CommentNotFoundException(NOT_FOUND_COMMENT));
    }
}
