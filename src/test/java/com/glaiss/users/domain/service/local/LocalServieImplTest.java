package com.glaiss.users.domain.service.local;

import com.glaiss.users.UsersApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LocalServiceImplTest extends UsersApplicationTest {

    @Autowired
    private LocalServiceImpl localService;

    @Nested
    class Dado_um_local extends UsersApplicationTest {
        @BeforeEach
        void setup() {
        }

        @Nested
        class Quando_salvar extends UsersApplicationTest {
            @BeforeEach
            void setup() {
            }

            @Test
            void Entao_deve_ter_sucesso() {

            }
        }
    }

    @Nested
    class Dado_locais_salvos extends UsersApplicationTest {
        @BeforeEach
        void setup() {
        }

        @Nested
        class Quando_listar extends UsersApplicationTest {
            @BeforeEach
            void setup() {
            }

            @Test
            void Entao_deve_ter_sucesso() {

            }
        }
    }

    @Nested
    class Dado_local_salvo extends UsersApplicationTest {
        @BeforeEach
        void setup() {
        }

        @Nested
        class Quando_buscar_por_id extends UsersApplicationTest {
            @BeforeEach
            void setup() {
            }

            @Test
            void Entao_deve_ter_sucesso() {

            }
        }
    }
}