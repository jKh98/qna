package com.jkh98.qna.repository;

import com.jkh98.qna.model.Answer;
import com.jkh98.qna.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Answer> findByCategoryId(UUID categoryId);

    List<Answer> findByUserId(UUID userId);
}
