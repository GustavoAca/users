package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.CredencialException;
import com.glaiss.core.security.Privilegio;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.controller.dto.DadosToken;
import com.glaiss.users.controller.dto.LoginResponse;
import com.glaiss.users.controller.dto.RefreshTokenDto;
import com.glaiss.users.domain.model.RefreshToken;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.service.refreshtoken.RefreshTokenServiceImpl;
import com.glaiss.users.domain.service.refreshtoken.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest extends UsersApplicationTest {


    private JwtEncoder jwtEncoder;
    private RefreshTokenServiceImpl refreshTokenService;
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        jwtEncoder = mock(JwtEncoder.class);
        refreshTokenService = mock(RefreshTokenServiceImpl.class);
        tokenService = new TokenService(jwtEncoder, refreshTokenService);
    }

    private Jwt jwtOf(String tokenValue) {
        return new Jwt(tokenValue, Instant.now(), Instant.now().plusSeconds(60),
                Map.of("alg", "none"), Map.of("sub", "sub"));
    }

    @Nested
    class Quando_criar_tokens_para_usuario {
        @Test
        void Entao_deve_retornar_LoginResponse_e_chamar_refresh_service() {
            Usuario usuario = mock(Usuario.class);
            RefreshToken created = new RefreshToken();
            created.setToken("rt-domain-token");
            when(usuario.getPrivilegio()).thenReturn(Privilegio.ROLE_FREE);

            when(jwtEncoder.encode(ArgumentMatchers.any(JwtEncoderParameters.class)))
                    .thenReturn(jwtOf("accessJwt"))
                    .thenReturn(jwtOf("refreshJwt"));

            when(refreshTokenService.criar(usuario)).thenReturn(created);

            LoginResponse resp = tokenService.criarTokens(usuario);

            assertNotNull(resp);
            assertEquals(60, resp.expiresIn());
            assertEquals("accessJwt", resp.accessToken());
            assertEquals("refreshJwt", resp.refreshToken());
            verify(refreshTokenService, times(1)).criar(usuario);
            verify(jwtEncoder, times(2)).encode(ArgumentMatchers.any(JwtEncoderParameters.class));
        }
    }

    @Nested
    class Quando_criar_tokens_oauth_para_usuario {
        @Test
        void Entao_deve_retornar_DadosToken_e_chamar_refresh_service() {
            Usuario usuario = mock(Usuario.class);
            RefreshToken created = new RefreshToken();
            created.setToken("rt-domain-token");
            when(usuario.getPrivilegio()).thenReturn(Privilegio.ROLE_FREE);

            when(jwtEncoder.encode(ArgumentMatchers.any(JwtEncoderParameters.class)))
                    .thenReturn(jwtOf("accessOauth"))
                    .thenReturn(jwtOf("refreshOauth"));

            when(refreshTokenService.criar(usuario)).thenReturn(created);

            DadosToken resp = tokenService.criarTokensOauth(usuario);

            assertNotNull(resp);
            assertEquals("accessOauth", resp.token());
            assertEquals("refreshOauth", resp.refreshToken());
            verify(refreshTokenService, times(1)).criar(usuario);
            verify(jwtEncoder, times(2)).encode(ArgumentMatchers.any(JwtEncoderParameters.class));
        }
    }

    @Nested
    class Quando_renovar_token_valido {
        @Test
        void Entao_deve_retornar_LoginResponse() {
            RefreshToken refresh = new RefreshToken();
            refresh.setToken("rt-token");
            Usuario usuario = mock(Usuario.class);
            when(usuario.getPrivilegio()).thenReturn(Privilegio.ROLE_FREE);

            when(jwtEncoder.encode(ArgumentMatchers.any(JwtEncoderParameters.class)))
                    .thenReturn(jwtOf("newAccess"))
                    .thenReturn(jwtOf("newRefresh"));

            LoginResponse resp = tokenService.renovarToken(refresh, usuario);

            assertNotNull(resp);
            assertEquals("newAccess", resp.accessToken());
            assertEquals("newRefresh", resp.refreshToken());
            verify(jwtEncoder, times(2)).encode(ArgumentMatchers.any(JwtEncoderParameters.class));
        }
    }

    @Nested
    class Quando_renovar_token_encodificacao_falha {
        @Test
        void Entao_deve_lancar_CredencialException() {
            RefreshToken refresh = new RefreshToken();
            refresh.setToken("rt-token");

            Usuario usuario = mock(Usuario.class);
            when(usuario.getId()).thenReturn(UUID.randomUUID());
            when(usuario.getUsername()).thenReturn("user@x.com");
            when(usuario.getPrivilegio()).thenReturn(Privilegio.ROLE_FREE);


            when(jwtEncoder.encode(ArgumentMatchers.any(JwtEncoderParameters.class)))
                    .thenThrow(new RuntimeException("encode failed"));

            assertThrows(CredencialException.class, () -> tokenService.renovarToken(refresh, usuario));
            verify(jwtEncoder, atLeastOnce()).encode(ArgumentMatchers.any(JwtEncoderParameters.class));
        }
    }


    @Nested
    class Quando_validar_refresh_token_delega_para_service {
        @Test
        void Entao_deve_retornar_o_RefreshToken_quando_valido() throws AccessDeniedException {
            RefreshTokenDto dto = new RefreshTokenDto("jwt-refresh");
            RefreshToken retorno = new RefreshToken();
            when(refreshTokenService.validar(dto)).thenReturn(retorno);

            RefreshToken resp = tokenService.validarRefreshToken(dto);

            assertSame(retorno, resp);
            verify(refreshTokenService, times(1)).validar(dto);
        }

        @Test
        void Entao_deve_propagar_CredencialException_quando_service_lancar() throws AccessDeniedException {
            RefreshTokenDto dto = new RefreshTokenDto("jwt-refresh");
            when(refreshTokenService.validar(dto)).thenThrow(new CredencialException());

            assertThrows(CredencialException.class, () -> tokenService.validarRefreshToken(dto));
            verify(refreshTokenService, times(1)).validar(dto);
        }
    }
}