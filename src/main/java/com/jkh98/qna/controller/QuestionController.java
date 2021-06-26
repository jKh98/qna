package com.jkh98.qna.controller;

import com.jkh98.qna.exception.ResourceNotFoundException;
import com.jkh98.qna.model.Question;
import com.jkh98.qna.repository.CategoryRepository;
import com.jkh98.qna.repository.QuestionRepository;
import com.jkh98.qna.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/questions")
    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @GetMapping("/questions/{questionId}")
    public Question getQuestionById(@PathVariable UUID questionId) {
        Optional<Question> questionMaybe = questionRepository.findById(questionId);
        if (questionMaybe.isEmpty()) {
            throw new ResourceNotFoundException("Question not found with id " + questionId);
        }
        return questionMaybe.get();
    }

    @GetMapping("/categories/{categoryId}/questions")
    public Page<Question> getQuestionsByCategory(@PathVariable UUID categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id " + categoryId);
        }

        return questionRepository.findByCategoryId(categoryId, pageable);
    }

    @GetMapping("/users/{userId}/questions")
    public Page<Question> getQuestionsByUser(@PathVariable UUID userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }

        return questionRepository.findByUserId(userId, pageable);
    }

    @PostMapping("/categories/{categoryId}/questions")
    public Question createQuestion(@PathVariable UUID categoryId, @Valid @RequestBody Question question) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    question.setCategory(category);
                    return questionRepository.save(question);
                }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

    }

    @PutMapping("/categories/{categoryId}/questions/{questionId}")
    public Question updateQuestion(@PathVariable UUID categoryId,
                                   @PathVariable UUID questionId,
                                   @Valid @RequestBody Question questionRequest) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id " + questionId);
        }

        return questionRepository.findById(questionId)
                .map(question -> {
                    question.setTitle(questionRequest.getTitle());
                    question.setBody(questionRequest.getBody());
                    return questionRepository.save(question);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }

    @DeleteMapping("/categories/{categoryId}/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(
            @PathVariable UUID categoryId,
            @PathVariable UUID questionId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id " + questionId);
        }

        return questionRepository.findById(questionId)
                .map(question -> {
                    questionRepository.delete(question);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }
}