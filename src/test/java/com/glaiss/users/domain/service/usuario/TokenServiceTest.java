package com.glaiss.users.domain.service.usuario;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.domain.service.refreshtoken.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenServiceTest extends UsersApplicationTest {

    @Autowired
    private TokenService tokenService;

    @Nested
    class Dado_um_refresh_token_invalido {
        private String refreshToken;

        @BeforeEach
        void setup() {
            refreshToken = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJHTEFJU1MiLCJzdWIiOiJ1c2VySWQ6IGMwYTdiNWY1LWZjNzctNGFmZS1iZjczLTgyMTNiMzg2MmJmYiwgdXNlcm5hbWU6IGdhbGFzZGFsYXM1MEBnbWFpbC5jb20iLCJleHAiOjE3NjY4Nzk0NzAsImlhdCI6MTc2Njg3MzM3MCwiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIn0.XkgP6SPGWV9wgP-uCexkVLWVD7lHu1emeR5Chwaiy_WOfS5bIS69x2AM2xgw13GvRu5wuQB_--Ab2zHzYD8SYB3SI_QRzG37mnMGj5WnF-UDKv0xR8cz4NTIiF7YU4aouPfUOda7SRlKxXPNJZFR9sQED2dWgCIUEA5ZJmcKpPQPw6fukIcHZERvC26U-9-IlmwqWjNdtqdV6sEH1QawCYdVGSws8mPq30t7a3nTcd2fx_0GRH4_BK17Q65DJLLcAL7Re_1p7ehEtNX9XpStlArODgpvwDjgwJHYydO1TO-gU8dhYW-pDGWFowk4eMTQarFMYyUHw0bUpzrfFzh_6Q";
        }

        @Nested
        class Quando_validar_token {

            @Test
            void Entao_deve_disparar_excecao() {
                assertThrows(JWTVerificationException.class, () -> tokenService.verificarToken(refreshToken));
            }
        }
    }

    @Nested
    class Dado_um_refresh_token_valido {
        private String refreshToken;

        @BeforeEach
        void setup() {
            refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJHTEFJU1MiLCJzdWIiOiJ1c2VySWQ6IGRhYWViOTQ0LWRmY2YtNDRhMy05ZTUxLWQwYzBiOGUwOThmOSwgdXNlcm5hbWU6IGdhbGFzZGFsYXNAZ21haWwuY29tQTk1MTRGOTktMjQyNS00RDY0LUI5NDUtNDlGRDRCQzAxNzY4IiwiZXhwIjoxNzcxNDA4NzkzLCJpYXQiOjE3Njc4MDg3OTMsImF1dGhvcml0aWVzIjoiUk9MRV9GUkVFIn0.IzUE5f72sPgxtpBjJSG7_RsYUV-zEQo_Tzf2ySRmg7Y";
        }

        @Nested
        class Quando_validar_token_deve_disparar_excecao {
            private Map<String, String> refreshTokenVerificado;

            @BeforeEach
            void setup() {
                refreshTokenVerificado = tokenService.verificarToken(refreshToken);
            }

            @Test
            void Entao_deve_retornar_falso_quando_token_for_valido() {
                assertNotNull(refreshTokenVerificado);
                assertNotNull(refreshTokenVerificado.get("userId"));
                assertNotNull(refreshTokenVerificado.get("username"));
            }
        }
    }
}