package com.glaiss.users.infra.client.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.controller.dto.DadosToken;
import com.glaiss.users.domain.service.usuario.UsuarioComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OauthGoogleServiceImplTest extends UsersApplicationTest {

    private RestTemplate restTemplate;
    private UsuarioComponent usuarioComponent;
    private OauthGoogleServiceImpl service;

    private final String clientId = "client";
    private final String clientSecret = "secret";
    private final String redirectUri = "http://callback";

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        usuarioComponent = mock(UsuarioComponent.class);
        service = new OauthGoogleServiceImpl(
                restTemplate,
                usuarioComponent,
                clientId,
                clientSecret,
                redirectUri
        );
    }

    @Nested
    class Quando_gerar_url {

        @Test
        void Entao_deve_conter_clientId_redirectUri_e_scope() {
            String url = service.gerarUrl();

            assertTrue(url.contains("client_id=" + clientId));
            assertTrue(url.contains("redirect_uri=" + redirectUri));
            assertTrue(url.contains("userinfo.email"));
            assertTrue(url.contains("response_type=code"));
        }

        @Test
        void Entao_redirecionarParaOauth_deve_retornar_location_header() {
            HttpHeaders headers = service.redirecionarParaOauth();
            assertNotNull(headers.getLocation());
        }
    }

    @Nested
    class Quando_autenticar_com_code_valido {

        @Test
        void Entao_deve_retornar_DadosToken() {
            String idToken = "jwt-token";

            Map<String, Object> body = Map.of("id_token", idToken);
            when(restTemplate.exchange(
                    eq("https://oauth2.googleapis.com/token"),
                    eq(HttpMethod.POST),
                    any(HttpEntity.class),
                    eq(Map.class)
            )).thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

            DecodedJWT decodedJWT = mock(DecodedJWT.class);
            Claim emailClaim = mock(Claim.class);
            when(emailClaim.asString()).thenReturn("x@y.com");

            Claim nameClaim = mock(Claim.class);
            when(nameClaim.asString()).thenReturn("Nome Usuario");

            when(decodedJWT.getClaim("email")).thenReturn(emailClaim);
            when(decodedJWT.getClaim("name")).thenReturn(nameClaim);

            try (MockedStatic<JWT> jwtMock = mockStatic(JWT.class)) {
                jwtMock.when(() -> JWT.decode(idToken)).thenReturn(decodedJWT);

                DadosToken token = new DadosToken("access", "refresh");
                when(usuarioComponent.loginOauth(any())).thenReturn(token);

                DadosToken resp = service.autenticar("code");

                assertNotNull(resp);
                assertEquals("access", resp.token());
                assertEquals("refresh", resp.refreshToken());
            }
        }
    }

    @Nested
    class Quando_token_nao_e_retornado {

        @Test
        void Entao_deve_lancar_excecao_quando_body_nulo() {
            when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

            assertThrows(RegistroNaoEncontradoException.class,
                    () -> service.autenticar("code"));
        }

        @Test
        void Entao_deve_lancar_excecao_quando_id_token_nulo() {
            when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(Map.of(), HttpStatus.OK));

            assertThrows(RegistroNaoEncontradoException.class,
                    () -> service.autenticar("code"));
        }
    }

    @Nested
    class Quando_usuarioComponent_lanca_excecao {

        @Test
        void Entao_deve_propagar_excecao() {
            String idToken = "jwt";

            when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(Map.of("id_token", idToken), HttpStatus.OK));

            DecodedJWT decodedJWT = mock(DecodedJWT.class);
            Claim emailClaim = mock(Claim.class);
            when(emailClaim.asString()).thenReturn("x@y.com");

            Claim nameClaim = mock(Claim.class);
            when(nameClaim.asString()).thenReturn("Nome Usuario");

            when(decodedJWT.getClaim("email")).thenReturn(emailClaim);
            when(decodedJWT.getClaim("name")).thenReturn(nameClaim);

            try (MockedStatic<JWT> jwtMock = mockStatic(JWT.class)) {
                jwtMock.when(() -> JWT.decode(idToken)).thenReturn(decodedJWT);

                when(usuarioComponent.loginOauth(any()))
                        .thenThrow(new RuntimeException("erro"));

                assertThrows(RuntimeException.class,
                        () -> service.autenticar("code"));
            }
        }
    }
}