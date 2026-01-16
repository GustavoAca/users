package com.glaiss.users.infra.client.oauth;

import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.controller.dto.DadosToken;
import com.glaiss.users.domain.service.usuario.UsuarioComponent;
import com.glaiss.users.infra.client.dto.GithubAccessTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OauthGithubServiceImplTest extends UsersApplicationTest {

    private RestTemplate restTemplate;
    private UsuarioComponent usuarioComponent;
    private OauthGithubServiceImpl service;

    private final String clientId = "my-client";
    private final String clientSecret = "secret";
    private final String redirectUri = "http://callback";

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        usuarioComponent = mock(UsuarioComponent.class);
        service = new OauthGithubServiceImpl(restTemplate, usuarioComponent, clientId, clientSecret, redirectUri);
    }

    @Nested
    class Quando_gerar_url {
        @Test
        void Entao_deve_conter_clientId_redirectUri_e_scope() {
            String url = service.gerarUrl();
            assertTrue(url.contains("client_id=" + clientId));
            assertTrue(url.contains("redirect_uri=" + redirectUri));
            assertTrue(url.contains("scope=read:user,user:email,public_repo"));
        }

        @Test
        void Entao_redirecionarParaOauth_deve_retornar_location_header() {
            HttpHeaders headers = service.redirecionarParaOauth();
            assertNotNull(headers.getLocation());
            URI loc = headers.getLocation();
            assertTrue(loc.toString().contains("client_id=" + clientId));
            assertTrue(loc.toString().contains("redirect_uri=" + redirectUri));
        }
    }

    @Nested
    class Quando_autenticar_com_code_valido {
        @Test
        void Entao_deve_delegar_para_usuarioComponent_e_retornar_DadosToken() {
            String code = "valid-code";

            GithubAccessTokenResponse tokenResp = new GithubAccessTokenResponse();
            tokenResp.setAccessToken("gh-access-token");
            ResponseEntity<GithubAccessTokenResponse> tokenEntity = new ResponseEntity<>(tokenResp, HttpStatus.OK);
            when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                    .thenReturn(tokenEntity);

            Map<String, Object> perfil = Map.of("name", "Nome Github");
            ResponseEntity<Map> perfilEntity = new ResponseEntity<>(perfil, HttpStatus.OK);
            when(restTemplate.exchange(eq("https://api.github.com/user"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                    .thenReturn(perfilEntity);

            DadosEmail emailMock = mock(DadosEmail.class);
            when(emailMock.primary()).thenReturn(true);
            when(emailMock.verified()).thenReturn(true);
            when(emailMock.email()).thenReturn("user@example.com");
            DadosEmail[] emails = new DadosEmail[]{emailMock};
            ResponseEntity<DadosEmail[]> emailsEntity = new ResponseEntity<>(emails, HttpStatus.OK);
            when(restTemplate.exchange(eq("https://api.github.com/user/emails"), eq(HttpMethod.GET), any(HttpEntity.class), eq(DadosEmail[].class)))
                    .thenReturn(emailsEntity);

            DadosToken dadosToken = new DadosToken("accessJwt", "refreshJwt");
            when(usuarioComponent.loginOauth(any())).thenReturn(dadosToken);

            DadosToken resp = service.autenticar(code);

            assertNotNull(resp);
            assertEquals("accessJwt", resp.token());
            assertEquals("refreshJwt", resp.refreshToken());
            verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(GithubAccessTokenResponse.class));
            verify(restTemplate, times(1)).exchange(eq("https://api.github.com/user"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class));
            verify(restTemplate, times(1)).exchange(eq("https://api.github.com/user/emails"), eq(HttpMethod.GET), any(HttpEntity.class), eq(DadosEmail[].class));
            verify(usuarioComponent, times(1)).loginOauth(any());
        }
    }

    @Nested
    class Quando_obter_token_sem_resposta_ou_sem_access_token {
        @Test
        void Entao_deve_lancar_RegistroNaoEncontradoException_quando_body_nulo() {
            when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
            assertThrows(RegistroNaoEncontradoException.class, () -> service.autenticar("code"));
        }

        @Test
        void Entao_deve_lancar_RegistroNaoEncontradoException_quando_access_token_nulo() {
            GithubAccessTokenResponse tokenResp = new GithubAccessTokenResponse();
            tokenResp.setAccessToken(null);
            when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                    .thenReturn(new ResponseEntity<>(tokenResp, HttpStatus.OK));
            assertThrows(RegistroNaoEncontradoException.class, () -> service.autenticar("code"));
        }
    }

    @Nested
    class Quando_obter_dados_oauth_sem_emails {
        @Test
        void Entao_deve_lancar_RegistroNaoEncontradoException_quando_emails_nulos_ou_vazios() {
            GithubAccessTokenResponse tokenResp = new GithubAccessTokenResponse();
            tokenResp.setAccessToken("token");
            when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                    .thenReturn(new ResponseEntity<>(tokenResp, HttpStatus.OK));

            Map<String, Object> perfil = Map.of("name", "Nome");
            when(restTemplate.exchange(eq("https://api.github.com/user"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(perfil, HttpStatus.OK));

            when(restTemplate.exchange(eq("https://api.github.com/user/emails"), eq(HttpMethod.GET), any(HttpEntity.class), eq(DadosEmail[].class)))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

            assertThrows(RegistroNaoEncontradoException.class, () -> service.obterDadosOauth("code"));
        }
    }

    @Nested
    class Quando_obter_dados_oauth_sem_email_primario_verificado {
        @Test
        void Entao_deve_retornar_null_quando_nao_existir_primary_verified() {
            GithubAccessTokenResponse tokenResp = new GithubAccessTokenResponse();
            tokenResp.setAccessToken("token");
            when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                    .thenReturn(new ResponseEntity<>(tokenResp, HttpStatus.OK));

            Map<String, Object> perfil = Map.of("name", "Nome");
            when(restTemplate.exchange(eq("https://api.github.com/user"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(perfil, HttpStatus.OK));

            DadosEmail emailMock = mock(DadosEmail.class);
            when(emailMock.primary()).thenReturn(false);
            DadosEmail[] emails = new DadosEmail[]{emailMock};
            when(restTemplate.exchange(eq("https://api.github.com/user/emails"), eq(HttpMethod.GET), any(HttpEntity.class), eq(DadosEmail[].class)))
                    .thenReturn(new ResponseEntity<>(emails, HttpStatus.OK));

            DadosOauth dados = service.obterDadosOauth("code");
            assertNull(dados);
        }
    }

    @Nested
    class Quando_resttemplate_lanca_excecao {
        @Test
        void Entao_deve_propagar_excecao() {
            when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                    .thenThrow(new RuntimeException("comms error"));
            assertThrows(RuntimeException.class, () -> service.autenticar("code"));
        }
    }

    @Nested
    class Quando_usuarioComponent_lanca_excecao {
        @Test
        void Entao_deve_propagar_a_excecao() {
            GithubAccessTokenResponse tokenResp = new GithubAccessTokenResponse();
            tokenResp.setAccessToken("token");
            when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                    .thenReturn(new ResponseEntity<>(tokenResp, HttpStatus.OK));

            Map<String, Object> perfil = Map.of("name", "Nome");
            when(restTemplate.exchange(eq("https://api.github.com/user"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                    .thenReturn(new ResponseEntity<>(perfil, HttpStatus.OK));

            DadosEmail emailMock = mock(DadosEmail.class);
            when(emailMock.primary()).thenReturn(true);
            when(emailMock.verified()).thenReturn(true);
            when(emailMock.email()).thenReturn("user@example.com");
            DadosEmail[] emails = new DadosEmail[]{emailMock};
            when(restTemplate.exchange(eq("https://api.github.com/user/emails"), eq(HttpMethod.GET), any(HttpEntity.class), eq(DadosEmail[].class)))
                    .thenReturn(new ResponseEntity<>(emails, HttpStatus.OK));

            when(usuarioComponent.loginOauth(any())).thenThrow(new RuntimeException("login error"));

            assertThrows(RuntimeException.class, () -> service.autenticar("code"));
        }
    }
}
