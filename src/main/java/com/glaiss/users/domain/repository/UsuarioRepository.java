package com.glaiss.users.domain.repository;

import com.glaiss.core.domain.repository.BaseRepository;
import com.glaiss.users.domain.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, UUID> {

    Optional<Usuario> findByUsername(String username);
}
