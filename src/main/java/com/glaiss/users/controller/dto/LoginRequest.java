package com.glaiss.users.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(@Valid @NotNull String username, @Valid @NotNull String password) {
}
