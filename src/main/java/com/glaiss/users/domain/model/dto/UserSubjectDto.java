package com.glaiss.users.domain.model.dto;

import lombok.Builder;
import lombok.ToString;

import java.util.UUID;

@Builder
public record UserSubjectDto(UUID userId,
                             String username) {
}
