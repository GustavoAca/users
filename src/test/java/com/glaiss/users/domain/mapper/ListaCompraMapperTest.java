package com.glaiss.users.domain.mapper;

import com.glaiss.users.MockFactory;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.domain.model.ListaCompra;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ListaCompraMapperTest extends UsersApplicationTest {

    @Autowired
    private ListaCompraMapper listaCompraMapper;

    @Autowired
    private MockFactory mockFactory;

    @Nested
    class Dado_uma_entidade extends UsersApplicationTest {
        private ListaCompra listaCompra;

        @BeforeEach
        void setup() {
            listaCompra = mockFactory.construirListaCompra();
        }

        @Nested
        class Quando_ser_convertido_para_dto extends UsersApplicationTest {
            private ListaCompraDto listaCompraDto;

            @BeforeEach
            void setup() {
                listaCompraDto = listaCompraMapper.toDto(listaCompra);
            }

            @Test
            void Entao_deve_ser_transformado_com_sucesso() {
                assertNotNull(listaCompraDto.getId());
                assertNotNull(listaCompraDto.getUsuarioId());
                assertNotNull(listaCompraDto.getValorTotal());
                assertEquals(listaCompra.getId(), listaCompraDto.getId());
                assertEquals(listaCompra.getUsuarioId(), listaCompraDto.getUsuarioId());
                assertEquals(listaCompra.getValorTotal(), listaCompraDto.getValorTotal());
            }
        }
    }

    @Nested
    class Dado_um_dto extends UsersApplicationTest {
        private ListaCompraDto listaCompraDto;

        @BeforeEach
        void setup() {
            listaCompraDto = mockFactory.construirListaCompraDto();
        }

        @Nested
        class Quando_ser_convertido_para_entidade extends UsersApplicationTest {
            private ListaCompra listaCompra;

            @BeforeEach
            void setup() {
                listaCompra = listaCompraMapper.toEntity(listaCompraDto);
            }

            @Test
            void Entao_deve_ser_transformado_com_sucesso() {
                assertNotNull(listaCompra.getId());
                assertNotNull(listaCompra.getUsuarioId());
                assertNotNull(listaCompra.getValorTotal());
                assertEquals(listaCompraDto.getId(), listaCompra.getId());
                assertEquals(listaCompraDto.getUsuarioId(), listaCompra.getUsuarioId());
                assertEquals(listaCompraDto.getValorTotal(), listaCompra.getValorTotal());
            }
        }
    }
}