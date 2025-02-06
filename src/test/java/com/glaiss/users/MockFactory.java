package com.glaiss.users;

import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.domain.model.ListaCompra;
import com.glaiss.users.domain.model.Local;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import com.glaiss.users.domain.model.dto.LocalDto;
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
}
