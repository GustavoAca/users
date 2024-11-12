package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.domain.service.BaseService;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.controller.dto.CreateUserDto;

import java.util.UUID;

public interface UsuarioService extends BaseService<Usuario, UUID> {
    void cadastrarUsuario(CreateUserDto createUserDto);
}
