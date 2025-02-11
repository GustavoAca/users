package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.CredencialException;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.MockFactory;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.controller.dto.LoginRequest;
import com.glaiss.users.controller.dto.LoginResponse;
import com.glaiss.users.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class TokenComponentTest extends UsersApplicationTest {

    @Autowired
    private TokenComponent tokenComponent;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MockFactory mockFactory;

    @Nested
    class Dado_usuario_existente extends UsersApplicationTest {
        private CreateUserDto createUserDto;

        @BeforeEach
        void setup() {
            createUserDto = mockFactory.construirCreatedUser();
            usuarioService.cadastrarUsuario(createUserDto);
        }

        @Nested
        class Quando_realizar_login extends UsersApplicationTest {
            private LoginResponse loginResponse;

            @BeforeEach
            void setup() {
                loginResponse = tokenComponent.login(new LoginRequest(createUserDto.username(), createUserDto.password()));
            }

            @Test
            void Entao_deve_ter_sucesso() {
                assertNotNull(loginResponse.accessToken());
                assertNotNull(loginResponse.expiresIn());
            }
        }

        @Nested
        class Quando_realizar_login_com_credenciais_erradas extends UsersApplicationTest {

            @Test
            void Entao_deve_ter_sucesso() {
                assertThrows(CredencialException.class, () -> tokenComponent.login(new LoginRequest(createUserDto.username(), "4321")));
            }
        }

        @Nested
        class Quando_deletar_usuario extends UsersApplicationTest {
            private boolean isUsuarioExiste;

            @BeforeEach
            void setup() {
                Usuario usuario = usuarioService.findByUsername(createUserDto.username())
                        .orElseThrow(() -> new RegistroNaoEncontradoException(createUserDto.getClass().getName()));

                isUsuarioExiste = tokenComponent.deletarUsuario(usuario.getId());
            }

            @Test
            void Entao_deve_ter_sucesso() {
                assertFalse(isUsuarioExiste);
            }
        }
    }

    @Nested
    class Dado_usuario_nao_cadastrado extends UsersApplicationTest {
        private CreateUserDto createUserDto;

        @BeforeEach
        void setup() {
            createUserDto = mockFactory.construirCreatedUser();
        }

        @Nested
        class Quando_tentar_logar extends UsersApplicationTest {

            @Test
            void Entao_deve_receber_excessao() {
                assertThrows(RegistroNaoEncontradoException.class,
                        () -> tokenComponent.login(
                                new LoginRequest(createUserDto.username(), createUserDto.password())));
            }
        }
    }

}