package com.glaiss.users.domain.service.listascompra;

import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ListaCompraServiceImplTest extends UsersApplicationTest {

    @Autowired
    private ListaCompraService listaCompraService;

    @Nested
    class Dado_um_usuario_iniciando_uma_lista extends UsersApplicationTest {

        @Nested
        class Quando_iniciar extends UsersApplicationTest {
            private ListaCompraDto listaCompraDto;

            @BeforeEach
            void setup() {
                listaCompraDto = listaCompraService.salvar();
            }

            @Test
            void Entao_deve_ser_criada_com_sucesso() {
                assertNotNull(listaCompraDto);
                assertNotNull(listaCompraDto.getUsuarioId());
                assertNotNull(listaCompraDto.getValorTotal());
                assertNotNull(listaCompraDto.getId());
            }
        }
    }
}