package com.glaiss.users.domain.mapper;

import com.glaiss.users.MockFactory;
import com.glaiss.users.UsersApplicationTests;
import com.glaiss.users.domain.model.Local;
import com.glaiss.users.domain.model.dto.LocalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalMapperTest extends UsersApplicationTests {
    @Autowired
    private LocalMapper localMapper;

    @Autowired
    private MockFactory mockFactory;

    @Nested
    class Dado_uma_entidade extends UsersApplicationTests {
        private Local local;

        @BeforeEach
        void setup() {
            local = mockFactory.construirLocal();
        }

        @Nested
        class Quando_ser_convertido_para_dto extends UsersApplicationTests {
            private LocalDto localDto;

            @BeforeEach
            void setup() {
                localDto = localMapper.toDto(local);
            }

            @Test
            void Deve_ser_transformado_com_sucesso() {
                assertNotNull(local.getId());
                assertNotNull(local.getNome());
                assertNotNull(local.getEndereco());
                assertNotNull(local.getCep());
                assertNotNull(local.getLogradouro());
                assertNotNull(local.getBairro());
                assertNotNull(local.getNumero());
                assertNotNull(local.getEstado());
                assertEquals(localDto.getId(), local.getId());
                assertEquals(localDto.getNome(), local.getNome());
                assertEquals(localDto.getEndereco(), local.getEndereco());
                assertEquals(localDto.getCep(), local.getCep());
                assertEquals(localDto.getLogradouro(), local.getLogradouro());
                assertEquals(localDto.getBairro(), local.getBairro());
                assertEquals(localDto.getNumero(), local.getNumero());
                assertEquals(localDto.getEstado(), local.getEstado());
            }
        }
    }

    @Nested
    class Dado_um_dto extends UsersApplicationTests {
        private LocalDto localDto;

        @BeforeEach
        void setup() {
            localDto = mockFactory.construirLocalDto();
        }

        @Nested
        class Quando_ser_convertido_para_entidade extends UsersApplicationTests {
            private Local local;

            @BeforeEach
            void setup() {
                local = localMapper.toEntity(localDto);
            }

            @Test
            void Deve_ser_transformado_com_sucesso() {
                assertNotNull(local.getId());
                assertNotNull(local.getNome());
                assertNotNull(local.getEndereco());
                assertNotNull(local.getCep());
                assertNotNull(local.getLogradouro());
                assertNotNull(local.getBairro());
                assertNotNull(local.getNumero());
                assertNotNull(local.getEstado());
                assertEquals(localDto.getId(), local.getId());
                assertEquals(localDto.getNome(), local.getNome());
                assertEquals(localDto.getEndereco(), local.getEndereco());
                assertEquals(localDto.getCep(), local.getCep());
                assertEquals(localDto.getLogradouro(), local.getLogradouro());
                assertEquals(localDto.getBairro(), local.getBairro());
                assertEquals(localDto.getNumero(), local.getNumero());
                assertEquals(localDto.getEstado(), local.getEstado());
            }
        }
    }
}