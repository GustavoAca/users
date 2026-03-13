package com.glaiss.users.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AlterarUserDto(@NotBlank String username,
                             @NotBlank String senhaAntiga,
                             @NotBlank String senha) {
}
