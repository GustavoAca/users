package com.glaiss.users.infra.client.oauth;

public record DadosEmail(String email,
                         Boolean primary,
                         Boolean verified,
                         String visibility) {
}
