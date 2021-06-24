package com.jkh98.qna.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Answer {
    private final UUID id;

    @NotBlank
    private final String text;

    @NotBlank
    private final UUID userId;

    @NotBlank
    private final UUID questionId;


    public Answer(
            @JsonProperty("id") UUID id,
            @JsonProperty("text") String text,
            @JsonProperty("userId") UUID userId,
            @JsonProperty("questionId") UUID questionId
    ) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.questionId = questionId;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getQuestionId() {
        return questionId;
    }
}
