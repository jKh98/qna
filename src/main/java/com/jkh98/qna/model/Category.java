package com.jkh98.qna.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Category {
    private final UUID id;

    @NotBlank
    private final String name;

    public Category(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}