package com.glaiss.users.domain.model.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserSubjectRefreshTokenDto(UUID userId,
                                         String username,
                                         String refreshToken) {

    @Override
    public String toString() {
        return String.format("userId: %s, username: %s, refreshToken: %s", userId, username, refreshToken);
    }
}
