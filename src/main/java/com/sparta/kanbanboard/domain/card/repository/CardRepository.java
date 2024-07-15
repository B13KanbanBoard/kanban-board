package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.common.exception.customexception.CardNotFoundException;
import com.sparta.kanbanboard.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static com.sparta.kanbanboard.common.exception.errorCode.CommonErrorCode.CARD_NOT_FOUND;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    default Card findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(()-> new CardNotFoundException(CARD_NOT_FOUND));
    }
}
