package com.glaiss.users.controller.dto;

import com.glaiss.core.security.Privilegio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateUserDto(@Valid @NotNull String username,
                            @Valid @NotNull String nome,
                            @Valid @NotNull String password) {
}