package com.glaiss.users;

import com.glaiss.core.security.Privilegio;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.infra.client.oauth.DadosOauth;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockFactory {

    public CreateUserDto construirCreatedUser() {
        return new CreateUserDto(String.format("galasdalas@gmail.com%S", UUID.randomUUID()),
                "Gustavo",
                "1234");
    }

    public CreateUserDto construirCreatedUserExistente() {
        return new CreateUserDto("galasdalas50@gmail.com",
                "Gustavo",
                "1234");
    }

    public Usuario construirUsarioExistente() {
        return Usuario.builder()
                .username("galasdalas1@gmail.com")
                .privilegio(Privilegio.ROLE_BASIC)
                .password("1234")
                .build();
    }

    public DadosOauth construirDadosOauth() {
        return new DadosOauth("galasdalas50gmail.com", "Gustavo");
    }
}
