package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.RegistroJaCadastradoException;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.core.security.Privilegio;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplUnitTest extends UsersApplicationTest {

    @Mock
    private UsuarioRepository repo;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setup() {
        usuarioService = new UsuarioServiceImpl(repo, bCryptPasswordEncoder);
    }

    @Nested
    class Quando_cadastrar_usuario_inexistente {
        @Test
        void Entao_deve_salvar_usuario_com_senha_codificada_e_role_free() {
            CreateUserDto dto = new CreateUserDto("novo@ex.com", "Nome Novo", "plainPwd");
            when(repo.findByUsername(dto.username())).thenReturn(Optional.empty());
            when(bCryptPasswordEncoder.encode(dto.password())).thenReturn("encodedPwd");
            when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            usuarioService.cadastrarUsuario(dto);

            ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
            verify(repo, times(1)).save(captor.capture());
            Usuario salvo = captor.getValue();

            assertEquals(dto.username(), salvo.getUsername());
            assertEquals("encodedPwd", salvo.getPassword());
            assertEquals(Privilegio.ROLE_FREE, salvo.getPrivilegio());
        }
    }

    @Nested
    class Quando_cadastrar_usuario_existente {
        @Test
        void Entao_deve_lancar_RegistroJaCadastradoException() {
            CreateUserDto dto = new CreateUserDto("exist@ex.com", "Nome", "pwd");
            Usuario existente = new Usuario();
            existente.setUsername(dto.username());
            when(repo.findByUsername(dto.username())).thenReturn(Optional.of(existente));

            assertThrows(RegistroJaCadastradoException.class, () -> usuarioService.cadastrarUsuario(dto));
            verify(repo, never()).save(any());
        }
    }

    @Nested
    class Quando_findByUsername {
        @Test
        void Entao_deve_delegar_para_o_repo() {
            String username = "u@x.com";
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            when(repo.findByUsername(username)).thenReturn(Optional.of(usuario));

            Optional<Usuario> resp = usuarioService.findByUsername(username);

            assertTrue(resp.isPresent());
            assertSame(usuario, resp.get());
            verify(repo, times(1)).findByUsername(username);
        }
    }

    @Nested
    class Quando_deletar_usuario_existente {
        @Test
        void Entao_deve_marcar_inativo_e_salvar_retorna_true() {
            UUID id = UUID.randomUUID();
            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setIsAtivo(Boolean.TRUE);

            when(repo.findById(id)).thenReturn(Optional.of(usuario));
            when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            Boolean res = usuarioService.deletar(id);

            assertTrue(res);
            assertFalse(usuario.getIsAtivo());
            verify(repo, times(1)).save(usuario);
        }
    }

    @Nested
    class Quando_deletar_usuario_inexistente {
        @Test
        void Entao_deve_lancar_RegistroNaoEncontradoException() {
            UUID id = UUID.randomUUID();
            when(repo.findById(id)).thenReturn(Optional.empty());

            assertThrows(RegistroNaoEncontradoException.class, () -> usuarioService.deletar(id));
            verify(repo, never()).save(any());
        }
    }

    @Nested
    class Quando_deletar_usuario_e_salvar_falhar {
        @Test
        void Entao_deve_retornar_false() {
            UUID id = UUID.randomUUID();
            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setIsAtivo(Boolean.TRUE);

            when(repo.findById(id)).thenReturn(Optional.of(usuario));
            when(repo.save(any())).thenThrow(new RuntimeException("db error"));

            Boolean res = usuarioService.deletar(id);

            assertFalse(res);
            // isAtivo foi setado para false antes da tentativa de salvar
            assertFalse(usuario.getIsAtivo());
            verify(repo, times(1)).save(usuario);
        }
    }
}
