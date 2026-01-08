package com.glaiss.users.domain.service.usuario;


import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.core.exception.RegistroJaCadastradoException;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.core.security.Privilegio;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, UUID, UsuarioRepository> implements UsuarioService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    protected UsuarioServiceImpl(UsuarioRepository repo,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(repo);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public void cadastrarUsuario(CreateUserDto createUserDto) {
        Optional<Usuario> usuarioEncontradoNoBanco = repo.findByUsername(createUserDto.username());

        if (usuarioEncontradoNoBanco.isPresent()) {
            log.error("Usuario existente {}", createUserDto.username());
            throw new RegistroJaCadastradoException(String.format("Usuario %s existente", createUserDto.username()));
        }

        super.salvar(Usuario.builder()
                .username(createUserDto.username())
                .nome(createUserDto.nome())
                .password(bCryptPasswordEncoder.encode(createUserDto.password()))
                .privilegio(Privilegio.ROLE_FREE)
                .build());
    }

    @Override
    public Boolean deletar(UUID usuarioId) {
        Usuario usuarioEncontrado = repo.findById(usuarioId)
                .orElseThrow(() -> new RegistroNaoEncontradoException(Usuario.class.getName(), usuarioId.toString()));
        usuarioEncontrado.setIsAtivo(Boolean.FALSE);

        try {
            super.salvar(usuarioEncontrado);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
