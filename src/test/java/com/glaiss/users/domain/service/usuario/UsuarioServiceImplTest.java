package com.glaiss.users.domain.service.usuario;

import com.glaiss.users.controller.dto.AlterarUserDto;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository repo;

    @Mock
    private UsuarioComponent usuarioComponent;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Nested
    @DisplayName("Testes para salvar usuário")
    class SalvarUsuario {

        @Test
        @DisplayName("Dado um novo usuário, quando salvar, então deve persistir com sucesso")
        void dadoNovoUsuario_quandoSalvar_entaoDevePersistirComSucesso() {
            // Dado
            CreateUserDto dto = new CreateUserDto("teste@example.com", "senha123", "Teste");
            Usuario usuario = Usuario.builder()
                    .username(dto.username())
                    .nome(dto.nome())
                    .build();

            when(repo.save(any(Usuario.class))).thenReturn(usuario);

            // Quando
            Usuario resultado = usuarioService.salvar(usuario);

            // Então
            assertThat(resultado.getUsername()).isEqualTo(dto.username());
            verify(repo, times(1)).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("Testes para buscar e atualizar")
    class BuscarEAtualizar {

        @Test
        @DisplayName("Dado um ID existente, quando buscar por ID, então deve retornar usuário")
        void dadoIdExistente_quandoBuscarPorId_entaoDeveRetornarUsuario() {
            // Dado
            UUID id = UUID.randomUUID();
            Usuario usuario = Usuario.builder().id(id).build();
            when(repo.findById(id)).thenReturn(Optional.of(usuario));

            // Quando
            Optional<Usuario> resultado = usuarioService.buscarPorId(id);

            // Então
            assertThat(resultado.get().getId()).isEqualTo(id);
        }
    }
}
