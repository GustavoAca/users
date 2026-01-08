package com.glaiss.users.domain.service.refreshtoken;

import com.glaiss.core.domain.service.BaseService;
import com.glaiss.users.domain.model.RefreshToken;
import com.glaiss.users.domain.model.Usuario;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

public interface RefreshTokenService extends BaseService<RefreshToken, UUID> {

    RefreshToken validar(String token) throws AccessDeniedException;

    RefreshToken criar(Usuario usuario);

    void revogar(RefreshToken refresh);
}
