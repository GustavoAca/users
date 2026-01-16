package com.glaiss.users.domain.service.refreshtoken;

import com.glaiss.users.controller.dto.RefreshTokenDto;
import com.glaiss.users.domain.model.RefreshToken;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RefreshTokenServiceImplTest {

    private RefreshTokenRepository repo;
    private RefreshTokenServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(RefreshTokenRepository.class);
        service = new RefreshTokenServiceImpl(repo);
    }

    @Nested
    class Quando_criar_refresh_token_quando_existente {
        private RefreshToken existente;
        private Usuario usuario;
        private RefreshToken resultado;

        @BeforeEach
        void setup() {
            usuario = mock(Usuario.class);
            UUID usuarioId = UUID.randomUUID();
            when(usuario.getId()).thenReturn(usuarioId);

            existente = new RefreshToken();
            existente.setToken("token-existente");
            existente.setUsuario(usuario);
            existente.setExpiresAt(Instant.now().plus(Duration.ofDays(10)));
            existente.setRevoked(false);

            when(repo.findByUsuarioId(usuarioId)).thenReturn(Optional.of(existente));

            resultado = service.criar(usuario);
        }

        @Test
        void Entao_deve_retornar_o_refresh_existente_e_nao_criar_novo() {
            assertSame(existente, resultado);
            verify(repo, never()).save(any());
        }
    }

    @Nested
    class Quando_criar_refresh_token_quando_inexistente {
        private Usuario usuario;
        private RefreshToken resultado;

        @BeforeEach
        void setup() {
            usuario = mock(Usuario.class);
            UUID usuarioId = UUID.randomUUID();
            when(usuario.getId()).thenReturn(usuarioId);

            when(repo.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
            when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            resultado = service.criar(usuario);
        }

        @Test
        void Entao_deve_criar_e_salvar_um_novo_refresh_token() {
            assertNotNull(resultado);
            assertNotNull(resultado.getToken());
            assertFalse(resultado.isRevoked());
            assertNotNull(resultado.getExpiresAt());
            assertTrue(resultado.getExpiresAt().isAfter(Instant.now()));
            assertSame(usuario, resultado.getUsuario());
            verify(repo, times(1)).save(any());
        }
    }

    @Nested
    class Quando_validar_refresh_token_valido {
        private RefreshToken esperado;
        private RefreshTokenDto dto;
        private RefreshToken resultado;

        @BeforeEach
        void setup() throws Exception {
            esperado = new RefreshToken();
            esperado.setToken("token-valido");
            esperado.setExpiresAt(Instant.now().plus(Duration.ofDays(5)));
            esperado.setRevoked(false);

            dto = new RefreshTokenDto(esperado.getToken());

            when(repo.findByTokenAndRevokedFalse(esperado.getToken())).thenReturn(Optional.of(esperado));

            resultado = service.validar(dto);
        }

        @Test
        void Entao_deve_retornar_o_refresh_token() {
            assertSame(esperado, resultado);
            assertFalse(resultado.isRevoked());
        }
    }

    @Nested
    class Quando_revogar_refresh_token {
        private RefreshToken refresh;

        @BeforeEach
        void setup() {
            refresh = new RefreshToken();
            refresh.setRevoked(false);
            service.revogar(refresh);
        }

        @Test
        void Entao_deve_marcar_como_revogado() {
            assertTrue(refresh.isRevoked());
        }
    }

    @Nested
    class Quando_validar_refresh_token_invalido_nao_encontrado {
        private RefreshTokenDto dto;

        @BeforeEach
        void setup() {
            dto = new RefreshTokenDto("token-inexistente");
            when(repo.findByTokenAndRevokedFalse(dto.refreshToken())).thenReturn(Optional.empty());
        }

        @Test
        void Entao_deve_lancar_AccessDeniedException_com_mensagem_invalido() {
            AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> service.validar(dto));
            assertTrue(ex.getMessage().contains("Refresh token inválido"));
        }
    }

    @Nested
    class Quando_validar_refresh_token_expirado {
        private RefreshToken refresh;
        private RefreshTokenDto dto;

        @BeforeEach
        void setup() {
            refresh = new RefreshToken();
            refresh.setToken("token-expirado");
            refresh.setExpiresAt(Instant.now().minus(Duration.ofDays(1)));
            refresh.setRevoked(false);

            dto = new RefreshTokenDto(refresh.getToken());
            when(repo.findByTokenAndRevokedFalse(refresh.getToken())).thenReturn(Optional.of(refresh));
        }

        @Test
        void Entao_deve_marcar_revogado_e_lancar_AccessDeniedException_com_mensagem_expirado() {
            AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> service.validar(dto));
            assertTrue(refresh.isRevoked());
            assertTrue(ex.getMessage().contains("Refresh token expirado"));
        }
    }
}
