package com.glaiss.users.domain.service.local;

import com.glaiss.core.domain.model.ResponsePage;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.MockFactory;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.domain.model.dto.LocalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalServiceImplTest extends UsersApplicationTest {

    @Autowired
    private LocalServiceImpl localService;

    @Autowired
    private MockFactory mockFactory;

    @Nested
    class Dado_um_local extends UsersApplicationTest {
        private LocalDto localDto;

        @BeforeEach
        void setup() {
            localDto = mockFactory.construirLocalDto();
        }

        @Nested
        class Quando_salvar extends UsersApplicationTest {
            private LocalDto localSalvo;

            @BeforeEach
            void setup() {
                localSalvo = localService.salvar(localDto);
            }

            @Test
            void Entao_deve_ter_sucesso() {
                assertNotNull(localSalvo);
                assertEquals(localDto.getCep(), localSalvo.getCep());
            }
        }
    }

    @Nested
    class Dado_locais_salvos extends UsersApplicationTest {

        @BeforeEach
        void setup() {
            LocalDto localDto = mockFactory.construirLocalDto();
            localService.salvar(localDto);
        }

        @Nested
        class Quando_listar extends UsersApplicationTest {
            private ResponsePage<LocalDto> locaisPaginado;

            @BeforeEach
            void setup() {
                locaisPaginado = localService.listarPaginaDto(PageRequest.of(0, 2));
            }

            @Test
            void Entao_deve_ter_sucesso() {
                assertNotEquals(0L, locaisPaginado.getTotalElements());
            }
        }
    }

    @Nested
    class Dado_local_salvo extends UsersApplicationTest {
        private LocalDto localDto;
        private LocalDto localSalvo;

        @BeforeEach
        void setup() {
            localDto = mockFactory.construirLocalDto();
            localSalvo = localService.salvar(localDto);
        }

        @Nested
        class Quando_buscar_por_id extends UsersApplicationTest {
            @BeforeEach
            void setup() {
                localDto = localService.buscarPorIdDto(localSalvo.getId());
            }

            @Test
            void Entao_deve_ter_sucesso() {
                assertEquals(localSalvo.getId(), localDto.getId());
            }
        }

        @Nested
        class Quando_buscar_por_id_inexistente extends UsersApplicationTest {

            @Test
            void Entao_deve_ter_disparado_excessao() {
                assertThrows(RegistroNaoEncontradoException.class, () -> localService.buscarPorIdDto(UUID.randomUUID()));
            }
        }
    }
}