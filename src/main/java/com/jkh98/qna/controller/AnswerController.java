package com.jkh98.qna.controller;

import com.jkh98.qna.exception.ResourceNotFoundException;
import com.jkh98.qna.model.Answer;
import com.jkh98.qna.repository.AnswerRepository;
import com.jkh98.qna.repository.QuestionRepository;
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
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions/{questionId}/answers")
    public Page<Answer> getAnswersByQuestionId(@PathVariable UUID questionId, Pageable pageable) {
        return answerRepository.findByQuestionId(questionId, pageable);
    }

    @PostMapping("/questions/{questionId}/answers")
    public Answer addAnswer(@PathVariable UUID questionId,
                            @Valid @RequestBody Answer answer) {
        return questionRepository.findById(questionId)
                .map(question -> {
                    answer.setQuestion(question);
                    return answerRepository.save(answer);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }

    @GetMapping("/questions/{questionId}/answers/{answerId}")
    public Answer getAnswerById(@PathVariable UUID questionId, @PathVariable UUID answerId) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with id " + questionId);
        }

        Optional<Answer> answerMaybe = answerRepository.findById(answerId);
        if (answerMaybe.isEmpty()) {
            throw new ResourceNotFoundException("Answer not found with id " + answerId);
        }
        return answerMaybe.get();
    }

    @PutMapping("/questions/{questionId}/answers/{answerId}")
    public Answer updateAnswer(@PathVariable UUID questionId,
                               @PathVariable UUID answerId,
                               @Valid @RequestBody Answer answerRequest) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with id " + questionId);
        }

        return answerRepository.findById(answerId)
                .map(answer -> {
                    answer.setText(answerRequest.getText());
                    return answerRepository.save(answer);
                }).orElseThrow(() -> new ResourceNotFoundException("Answer not found with id " + answerId));
    }

    @DeleteMapping("/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable UUID questionId,
                                          @PathVariable UUID answerId) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with id " + questionId);
        }

        return answerRepository.findById(answerId)
                .map(answer -> {
                    answerRepository.delete(answer);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Answer not found with id " + answerId));

    }
}