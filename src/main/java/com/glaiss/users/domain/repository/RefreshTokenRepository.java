package com.glaiss.users.domain.repository;

import com.glaiss.core.domain.repository.BaseRepository;
import com.glaiss.users.domain.model.RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

    Optional<RefreshToken> findByUsuarioId(UUID usuarioId);
}
