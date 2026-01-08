package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.domain.service.BaseService;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.model.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioService extends BaseService<Usuario, UUID> {

    Optional<Usuario> findByUsername(String username);

    void cadastrarUsuario(CreateUserDto createUserDto);
}
