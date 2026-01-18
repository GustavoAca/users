// language: java
package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.CredencialException;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.core.security.Privilegio;
import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.controller.dto.*;
import com.glaiss.users.domain.model.RefreshToken;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.service.refreshtoken.TokenService;
import com.glaiss.users.infra.client.oauth.DadosOauth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioComponentTest extends UsersApplicationTest {

    private UsuarioService usuarioService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenService tokenService;
    private UsuarioComponent usuarioComponent;

    @BeforeEach
    void setup() {
        usuarioService = mock(UsuarioService.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        tokenService = mock(TokenService.class);
        usuarioComponent = new UsuarioComponent(usuarioService, bCryptPasswordEncoder, tokenService);
    }

    @Nested
    class Quando_realizar_login_com_credenciais_validas {
        private final String username = "user@example.com";
        private final String password = "senha123";
        private Usuario usuarioMock;

        @BeforeEach
        void setup() {
            usuarioMock = mock(Usuario.class);
            when(usuarioService.findByUsername(username)).thenReturn(Optional.of(usuarioMock));
            when(usuarioMock.isLoginCorretc(password, bCryptPasswordEncoder)).thenReturn(true);
            when(tokenService.criarTokens(usuarioMock)).thenReturn(new LoginResponse("access", 60, "refreshJwt"));
        }

        @Test
        void Entao_deve_retornar_tokens() {
            LoginResponse resp = usuarioComponent.login(new LoginRequest(username, password));
            assertNotNull(resp);
            assertNotNull(resp.accessToken());
            assertNotNull(resp.refreshToken());
            verify(tokenService, times(1)).criarTokens(usuarioMock);
        }
    }

    @Nested
    class Quando_realizar_login_com_senha_incorreta {
        private final String username = "user@example.com";
        private final String password = "senhaErrada";
        private Usuario usuarioMock;

        @BeforeEach
        void setup() {
            usuarioMock = mock(Usuario.class);
            when(usuarioService.findByUsername(username)).thenReturn(Optional.of(usuarioMock));
            when(usuarioMock.isLoginCorretc(password, bCryptPasswordEncoder)).thenReturn(false);
        }

        @Test
        void Entao_deve_lancar_CredencialException() {
            assertThrows(CredencialException.class, () -> usuarioComponent.login(new LoginRequest(username, password)));
            verify(tokenService, never()).criarTokens(any());
        }
    }

    @Nested
    class Quando_realizar_login_usuario_inexistente {
        @Test
        void Entao_deve_lancar_RegistroNaoEncontradoException() {
            String username = "naoexiste@example.com";
            when(usuarioService.findByUsername(username)).thenReturn(Optional.empty());
            assertThrows(RegistroNaoEncontradoException.class, () -> usuarioComponent.login(new LoginRequest(username, "qualquer")));
            verify(tokenService, never()).criarTokens(any());
        }
    }

    @Nested
    class Quando_login_oauth_usuario_existente {
        @Test
        void Entao_deve_retornar_tokens_e_nao_criar_usuario() {
            DadosOauth dados = new DadosOauth("existente@example.com", "Nome");
            Usuario usuarioMock = mock(Usuario.class);
            when(usuarioService.findByUsername(dados.email())).thenReturn(Optional.of(usuarioMock));
            when(tokenService.criarTokensOauth(usuarioMock)).thenReturn(new DadosToken("accessJwt", "refreshJwt", 1));

            DadosToken resp = usuarioComponent.loginOauth(dados);
            assertNotNull(resp);
            assertNotNull(resp.token());
            assertNotNull(resp.refreshToken());
            verify(usuarioService, never()).salvar(any());
            verify(tokenService, times(1)).criarTokensOauth(usuarioMock);
        }
    }

    @Nested
    class Quando_login_oauth_usuario_inexistente {
        @Test
        void Entao_deve_criar_usuario_com_role_free_e_retornar_tokens() {
            DadosOauth dados = new DadosOauth("novo@example.com", "Novo Nome");
            when(usuarioService.findByUsername(dados.email())).thenReturn(Optional.empty());
            when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPwd");
            when(usuarioService.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(tokenService.criarTokensOauth(any())).thenReturn(new DadosToken("access", "refresh", 1));

            DadosToken resp = usuarioComponent.loginOauth(dados);
            assertNotNull(resp);
            assertNotNull(resp.token());
            assertNotNull(resp.refreshToken());

            ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
            verify(usuarioService, times(1)).salvar(captor.capture());
            Usuario criado = captor.getValue();
            assertEquals(dados.email(), criado.getUsername());
            assertEquals(Privilegio.ROLE_FREE, criado.getPrivilegio());
            assertTrue(criado.getIsAtivo());
            verify(tokenService, times(1)).criarTokensOauth(any());
        }
    }

    @Nested
    class Quando_alterar_senha_com_usuario_autenticado {
        @Test
        void Entao_deve_atualizar_a_senha_e_retornar_true() {
            String ctxUsername = "ctx@example.com";
            String novaSenha = "novaSenha123";
            Usuario usuarioMock = mock(Usuario.class);
            when(usuarioService.findByUsername(ctxUsername)).thenReturn(Optional.of(usuarioMock));
            when(bCryptPasswordEncoder.encode(novaSenha)).thenReturn("encodedValue");
            when(usuarioService.salvar(usuarioMock)).thenReturn(usuarioMock);

            try (MockedStatic<SecurityContextUtils> sc = mockStatic(SecurityContextUtils.class)) {
                sc.when(SecurityContextUtils::getUsername).thenReturn(ctxUsername);
                boolean res = usuarioComponent.alterarSenha(new AlterarUserDto(ctxUsername, novaSenha));
                assertTrue(res);
            }

            verify(usuarioMock, times(1)).setPassword("encodedValue");
            verify(usuarioService, times(1)).salvar(usuarioMock);
        }
    }

    @Nested
    class Quando_alterar_senha_com_usuario_diferente_no_contexto {
        @Test
        void Entao_deve_retornar_false_e_nao_alterar_senha() {
            String ctxUsername = "ctx@example.com";
            String dtoUsername = "outro@example.com";
            String novaSenha = "senha";
            Usuario usuarioMock = mock(Usuario.class);
            // context retorna ctxUsername, o DTO tem outro username
            try (MockedStatic<SecurityContextUtils> sc = mockStatic(SecurityContextUtils.class)) {
                sc.when(SecurityContextUtils::getUsername).thenReturn(ctxUsername);
                boolean res = usuarioComponent.alterarSenha(new AlterarUserDto(dtoUsername, novaSenha));
                assertFalse(res);
            }
            verify(usuarioService, never()).findByUsername(anyString());
            verify(usuarioMock, never()).setPassword(anyString());
        }
    }

    @Nested
    class Quando_renovar_token_com_refresh_valido {
        @Test
        void Entao_deve_retornar_login_response_com_tokens() {
            RefreshToken refreshMock = mock(RefreshToken.class);
            Usuario usuarioMock = mock(Usuario.class);
            when(refreshMock.getUsuario()).thenReturn(usuarioMock);
            when(usuarioMock.getUsername()).thenReturn("u@x.com");

            when(tokenService.validarRefreshToken(any(RefreshTokenDto.class))).thenReturn(refreshMock);
            when(usuarioService.findByUsername("u@x.com")).thenReturn(Optional.of(usuarioMock));
            when(tokenService.renovarToken(refreshMock, usuarioMock)).thenReturn(new LoginResponse("newAccess", 60, "newRefresh"));

            LoginResponse resp = usuarioComponent.renovarToken(new RefreshTokenDto("qualquer"));
            assertNotNull(resp);
            assertNotNull(resp.accessToken());
            assertNotNull(resp.refreshToken());
            verify(tokenService, times(1)).validarRefreshToken(any());
            verify(tokenService, times(1)).renovarToken(refreshMock, usuarioMock);
        }
    }

    @Nested
    class Quando_renovar_token_com_refresh_invalido {
        @Test
        void Entao_deve_lancar_CredencialException() {
            when(tokenService.validarRefreshToken(any())).thenThrow(new CredencialException());
            assertThrows(CredencialException.class, () -> usuarioComponent.renovarToken(new RefreshTokenDto("invalido")));
            verify(tokenService, times(1)).validarRefreshToken(any());
            verify(tokenService, never()).renovarToken(any(), any());
        }
    }

    @Nested
    class Quando_deletar_usuario {
        @Test
        void Entao_deve_retornar_true_quando_existir() {
            UUID id = UUID.randomUUID();
            when(usuarioService.deletar(id)).thenReturn(Boolean.TRUE);
            Boolean res = usuarioComponent.deletarUsuario(id);
            assertTrue(res);
            verify(usuarioService, times(1)).deletar(id);
        }

        @Test
        void Entao_deve_lancar_excecao_quando_inexistir() {
            UUID id = UUID.randomUUID();
            when(usuarioService.deletar(id)).thenThrow(new RegistroNaoEncontradoException(Usuario.class.getName(), id.toString()));
            assertThrows(RegistroNaoEncontradoException.class, () -> usuarioComponent.deletarUsuario(id));
        }
    }
}
