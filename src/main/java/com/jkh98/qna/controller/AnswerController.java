package com.jkh98.qna.controller;

import com.jkh98.qna.exception.ForbiddenException;
import com.jkh98.qna.exception.ResourceNotFoundException;
import com.jkh98.qna.model.Answer;
import com.jkh98.qna.model.User;
import com.jkh98.qna.repository.AnswerRepository;
import com.jkh98.qna.repository.QuestionRepository;
import com.jkh98.qna.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/questions/{questionId}/answers")
    public Page<Answer> getAnswersByQuestionId(@PathVariable UUID questionId, Pageable pageable) {
        return answerRepository.findByQuestionId(questionId, pageable);
    }

    @PostMapping("/questions/{questionId}/answers")
    public Answer addAnswer(@PathVariable UUID questionId,
                            @Valid @RequestBody Answer answer) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + userDetails.getUsername()));

        return questionRepository.findById(questionId)
                .map(question -> {
                    answer.setUser(user);
                    answer.setQuestion(question);
                    return answerRepository.save(answer);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }

    @GetMapping("/users/{userId}/answers")
    public Page<Answer> getAnswersByUser(@PathVariable UUID userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }

        return answerRepository.findByUserId(userId, pageable);
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + userDetails.getUsername()));

        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with id " + questionId);
        }

        return answerRepository.findById(answerId)
                .map(answer -> {
                    if (!answer.getUser().equals(user)) {
                        throw new ForbiddenException("Cannot edit answer");
                    }

                    answer.setText(answerRequest.getText());
                    return answerRepository.save(answer);
                }).orElseThrow(() -> new ResourceNotFoundException("Answer not found with id " + answerId));
    }

    @DeleteMapping("/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable UUID questionId,
                                          @PathVariable UUID answerId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + userDetails.getUsername()));

        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with id " + questionId);
        }

        return answerRepository.findById(answerId)
                .map(answer -> {
                    if (!answer.getUser().equals(user)) {
                        throw new ForbiddenException("Cannot delete answer");
                    }

                    answerRepository.delete(answer);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Answer not found with id " + answerId));
    }
}