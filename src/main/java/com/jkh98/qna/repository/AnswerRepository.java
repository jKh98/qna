package com.jkh98.qna.repository;

import com.jkh98.qna.model.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    Page<Answer> findByQuestionId(UUID questionId, Pageable pageable);

    Page<Answer> findByUserId(UUID questionId, Pageable pageable);
}