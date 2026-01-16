package com.glaiss.users.controller.dto;

import jakarta.validation.Valid;

public record DadosToken(@Valid String token,
                         @Valid String refreshToken,
                         @Valid Integer expiresIn) {
}
