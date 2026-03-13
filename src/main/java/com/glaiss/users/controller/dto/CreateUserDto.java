package com.glaiss.users.controller.dto;

import com.glaiss.core.security.Privilegio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(@NotBlank String username,
                            @NotBlank String nome,
                            @NotBlank @Size(min = 8) String password) {
}