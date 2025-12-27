package com.glaiss.users.domain.service.listascompra;

import com.glaiss.core.domain.model.ResponsePage;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.MockFactory;
import com.glaiss.users.UsersApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListaCompraServiceImplTest extends UsersApplicationTest {

    @Autowired
    private ListaCompraService listaCompraService;

    @Autowired
    private MockFactory mockFactory;

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

    @Nested
    class Dado_lista_de_compra_salva extends UsersApplicationTest {
        private ListaCompraDto listaCompraDto;

        @BeforeEach
        void setup() {
            listaCompraDto = listaCompraService.salvar();
        }

        @Nested
        class Quando_buscar_por_id extends UsersApplicationTest {
            private ListaCompraDto listaEncontrada;

            @BeforeEach
            void setup() {
                listaEncontrada = listaCompraService.buscarPorIdDto(listaCompraDto.getId());
            }

            @Test
            void Entao_deve_ter_sucesso() {
                assertEquals(listaCompraDto.getId(), listaEncontrada.getId());
            }
        }

        @Nested
        class Quando_buscar_por_id_inexistente extends UsersApplicationTest {

            @Test
            void Entao_deve_ter_disparado_excessao() {
                assertThrows(RegistroNaoEncontradoException.class, () -> listaCompraService.buscarPorIdDto(UUID.randomUUID()));
            }
        }
    }


    @Nested
    class Dado_listas_salvas extends UsersApplicationTest {

        @BeforeEach
        void setup() {
            for (int i = 0; i < 2; i++) {
                listaCompraService.salvar();
            }
        }

        @Nested
        class Quando_listar extends UsersApplicationTest {
            private ResponsePage<ListaCompraDto> listaCompraDto;
            private Boolean isListaDoMesmoUsuario;

            @BeforeEach
            void setup() {
                listaCompraDto = listaCompraService.listarPaginaDto(PageRequest.of(0, 2));
                isListaDoMesmoUsuario = listaCompraDto.getContent().stream().allMatch(l -> l.getUsuarioId().equals(SecurityContextUtils.getId()));
            }

            @Test
            void Entao_deve_ter_sucesso() {
                assertNotEquals(0L, listaCompraDto.getTotalElements());
                assertTrue(isListaDoMesmoUsuario);
            }
        }
    }
}