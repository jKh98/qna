package com.jkh98.qna.repository;

import com.jkh98.qna.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    Page<Question> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<Question> findByUserId(UUID userId, Pageable pageable);
}
