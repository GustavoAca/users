package com.glaiss.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaiss.core.jpa.test.GlaissWebMvcTest;
import com.glaiss.users.config.SecurityConfig;
import com.glaiss.users.controller.dto.*;
import com.glaiss.users.domain.service.usuario.UsuarioComponent;
import com.glaiss.users.domain.repository.UsuarioRepository;
import com.glaiss.users.domain.repository.RefreshTokenRepository;
import com.glaiss.users.config.FiltroTokenAcesso;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.glaiss.core.security.test.GlaissMockUser;

@GlaissWebMvcTest(controllers = UsuarioController.class, 
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = FiltroTokenAcesso.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    },
    properties = {
        "spring.application.name=users",
        "jwt.public.key=classpath:application.yml",
        "jwt.private.key=classpath:application.yml",
        "servico-externos-permitidos=http://localhost:4200",
        "eureka.client.enabled=false"
})
@GlaissMockUser
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioComponent usuarioComponent;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Testes para UsuarioController")
    class UsuarioEndpoints {

        @Test
        @DisplayName("Dado um novo usuário, quando cadastrar, então deve retornar 200")
        void dadoNovoUsuario_quandoCadastrar_entaoDeveRetornar200() throws Exception {
            CreateUserDto dto = new CreateUserDto("teste@example.com", "Teste", "senha123");
            doNothing().when(usuarioComponent).cadastrarUsuario(any());

            mockMvc.perform(post("/usuarios/cadastrar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Dado credenciais válidas, quando login, então deve retornar LoginResponse")
        void dadoCredenciaisValidas_quandoLogin_entaoDeveRetornar200() throws Exception {
            LoginRequest request = new LoginRequest("teste@example.com", "senha123");
            LoginResponse response = new LoginResponse("token", 3600, "refreshToken");

            when(usuarioComponent.login(any())).thenReturn(response);

            mockMvc.perform(post("/usuarios/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Dado um ID válido, quando deletar, então deve retornar 200")
        void dadoIdValido_quandoDeletar_entaoDeveRetornar200() throws Exception {
            UUID id = UUID.randomUUID();
            when(usuarioComponent.deletarUsuario(id)).thenReturn(true);

            mockMvc.perform(delete("/usuarios/{id}", id))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Dado DTO de alteração, quando alterar senha, então deve retornar 200")
        void dadoAlteracaoValida_quandoAlterarSenha_entaoDeveRetornar200() throws Exception {
            AlterarUserDto dto = new AlterarUserDto("teste@example.com", "novaSenha");
            when(usuarioComponent.alterarSenha(any())).thenReturn(true);

            mockMvc.perform(put("/usuarios/alterar-senha")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
        }
    }
}
