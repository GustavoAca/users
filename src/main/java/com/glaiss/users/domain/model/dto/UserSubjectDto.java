package com.glaiss.users.domain.model.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserSubjectDto(UUID userId,
                             String username) {

    @Override
    public String toString() {
        return String.format("userId: %s, username: %s", userId, username);
    }
}
