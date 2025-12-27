package com.glaiss.users;

import com.glaiss.core.security.Privilegio;
import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.model.Usuario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class MockFactory {

    public ListaCompra construirListaCompra() {
        return ListaCompra.builder()
                .id(UUID.randomUUID())
                .usuarioId(SecurityContextUtils.getId())
                .valorTotal(BigDecimal.ZERO)
                .build();
    }

    public ListaCompraDto construirListaCompraDto() {
        return ListaCompraDto.builder()
                .id(UUID.randomUUID())
                .usuarioId(SecurityContextUtils.getId())
                .valorTotal(BigDecimal.ZERO)
                .build();
    }

    public Local construirLocal() {
        return Local.builder()
                .id(UUID.randomUUID())
                .nome("Shibata - Itaquaquecetuba")
                .endereco("Estrada de Santa Isabel")
                .cep("08577-770")
                .logradouro("Estrada de Santa Isabel")
                .bairro("Vila Japão")
                .numero("3055")
                .estado("SP")
                .build();
    }

    public LocalDto construirLocalDto() {
        return LocalDto.builder()
                .id(UUID.randomUUID())
                .nome("Shibata - Itaquaquecetuba")
                .endereco("Estrada de Santa Isabel")
                .cep("08577-770")
                .logradouro("Estrada de Santa Isabel")
                .bairro("Vila Japão")
                .numero("3055")
                .estado("SP")
                .build();
    }

    public CreateUserDto construirCreatedUser() {
        return new CreateUserDto(String.format("galasdalas@gmail.com%S", UUID.randomUUID()),
                Privilegio.ROLE_BASIC,
                "1234");
    }

    public CreateUserDto construirCreatedUserExistente() {
        return new CreateUserDto("galasdalas50@gmail.com",
                Privilegio.ROLE_BASIC,
                "1234");
    }

    public Usuario construirUsarioExistente() {
        return Usuario.builder()
                .username("galasdalas1@gmail.com")
                .privilegio(Privilegio.ROLE_BASIC)
                .password("1234")
                .build();
    }
}
