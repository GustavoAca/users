package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.RegistroJaCadastradoException;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.MockFactory;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceImplTest extends UsersApplicationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MockFactory mockFactory;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Nested
    class Dado_que_receba_um_usuario_inexistente extends UsersApplicationTest {
        private CreateUserDto createUserDto;

        @BeforeEach
        void setup() {
            createUserDto = mockFactory.construirCreatedUser();
        }

        @Nested
        class Quando_cadastrar extends UsersApplicationTest {
            private Usuario usuario;

            @BeforeEach
            void setup() {
                usuarioService.cadastrarUsuario(createUserDto);
                usuario = usuarioService.findByUsername(createUserDto.username())
                        .orElseThrow(() -> new RegistroNaoEncontradoException(createUserDto.username(), createUserDto.getClass().getName()));
            }

            @Test
            void Entao_deve_cadastrar_com_sucesso() {
                assertEquals(createUserDto.username(), usuario.getUsername());
                assertTrue(usuario.isLoginCorretc(createUserDto.password(), bCryptPasswordEncoder));
            }
        }
    }

    @Nested
    class Dado_que_receba_um_usuario_existente extends UsersApplicationTest {
        private CreateUserDto createUserDto;

        @BeforeEach
        void setup() {
            Usuario usuario = mockFactory.construirUsarioExistente();
            usuarioService.salvar(usuario);
            createUserDto = new CreateUserDto(usuario.getUsername(), usuario.getNome(), usuario.getPassword());
        }

        @Nested
        class Quando_cadastrar extends UsersApplicationTest {

            @Test
            void Entao_deve_receber_excessao() {
                assertThrows(RegistroJaCadastradoException.class, () -> usuarioService.cadastrarUsuario(createUserDto));
            }
        }
    }
}