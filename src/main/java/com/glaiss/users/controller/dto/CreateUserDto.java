package com.glaiss.users.controller.dto;

import com.glaiss.core.security.Privilegio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateUserDto(@Valid @NotNull String username,
                            @Valid @NotNull Privilegio privilegio,
                            @Valid @NotNull String password) {
}