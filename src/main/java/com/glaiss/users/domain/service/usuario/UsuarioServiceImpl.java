package com.glaiss.users.domain.service.usuario;


import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.core.exception.RegistroJaCadastradoException;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.repository.UsuarioRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, UUID, UsuarioRepositoy> implements UsuarioService {


    @Autowired
    protected UsuarioServiceImpl(UsuarioRepositoy repo) {
        super(repo);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public void cadastrarUsuario(CreateUserDto createUserDto) {
        Optional<Usuario> usuarioEncontradoNoBanco = repo.findByUsername(createUserDto.username());

        if (usuarioEncontradoNoBanco.isPresent()) {
            throw new RegistroJaCadastradoException(String.format("Usuario %s existente", createUserDto.username()));
        }

        super.salvar(Usuario.builder()
                .username(createUserDto.username())
                .password(createUserDto.password())
                .privilegio(createUserDto.privilegio())
                .build());
    }
}
