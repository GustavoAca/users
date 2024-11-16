package com.glaiss.users.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
