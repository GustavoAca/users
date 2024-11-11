package com.glaiss.users.service.usuario;

import com.glaiss.core.service.BaseServiceImpl;
import com.glaiss.users.model.Usuario;
import com.glaiss.users.repository.UsuarioRepositoy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, UUID, UsuarioRepositoy> implements UsuarioService {

    protected UsuarioServiceImpl(UsuarioRepositoy repo) {
        super(repo);
    }
}
