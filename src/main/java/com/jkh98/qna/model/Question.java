package com.jkh98.qna.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Question {
    private final UUID id;

    @NotBlank
    private final String title;

    @NotBlank
    private final String body;

    @NotBlank
    private final UUID categoryId;

    @NotBlank
    private final UUID userId;


    public Question(
            @JsonProperty("id") UUID id,
            @JsonProperty("title") String title,
            @JsonProperty("body") String body,
            @JsonProperty("categoryId") UUID categoryId,
            @JsonProperty("userId") UUID userId
    ) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public UUID getUserId() {
        return userId;
    }
}
