package com.glaiss.users.repository;

import com.glaiss.core.repository.BaseRepository;
import com.glaiss.users.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepositoy extends BaseRepository<Usuario, UUID> {
}
