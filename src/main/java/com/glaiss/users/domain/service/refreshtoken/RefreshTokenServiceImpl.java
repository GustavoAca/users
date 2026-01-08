package com.glaiss.users.domain.service.refreshtoken;

import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.users.domain.model.RefreshToken;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl extends BaseServiceImpl<RefreshToken, UUID, RefreshTokenRepository> implements RefreshTokenService {
    private static final Duration REFRESH_TTL = Duration.ofDays(30);

    protected RefreshTokenServiceImpl(RefreshTokenRepository repo) {
        super(repo);
    }

    @Override
    public RefreshToken criar(Usuario usuario) {
        Optional<RefreshToken> refreshToken = repo.findByUsuarioId(usuario.getId());

        if(refreshToken.isPresent()){
            return refreshToken.get();
        }

        RefreshToken refresh = new RefreshToken();
        refresh.setToken(UUID.randomUUID().toString());
        refresh.setUsuario(usuario);
        refresh.setExpiresAt(Instant.now().plus(REFRESH_TTL));
        refresh.setRevoked(false);

        return repo.save(refresh);
    }

    @Override
    public RefreshToken validar(String token) throws AccessDeniedException {

        RefreshToken refresh = repo.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new AccessDeniedException("Refresh token inválido"));

        if (refresh.getExpiresAt().isBefore(Instant.now())) {
            refresh.setRevoked(true);
            throw new AccessDeniedException("Refresh token expirado");
        }

        return refresh;
    }

    @Override
    public void revogar(RefreshToken refresh) {
        refresh.setRevoked(true);
    }
}
