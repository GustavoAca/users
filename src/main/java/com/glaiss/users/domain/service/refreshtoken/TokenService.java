package com.glaiss.users.domain.service.refreshtoken;

import com.glaiss.core.exception.CredencialException;
import com.glaiss.users.controller.dto.DadosToken;
import com.glaiss.users.controller.dto.LoginResponse;
import com.glaiss.users.domain.model.RefreshToken;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.model.dto.UserSubjectDto;
import com.glaiss.users.domain.model.dto.UserSubjectRefreshTokenDto;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final static Integer EXPIRES_IN = 60;

    private final JwtEncoder jwtEncoder;
    private final RefreshTokenService refreshTokenService;

    public TokenService(JwtEncoder jwtEncoder,
                        RefreshTokenService refreshTokenService) {
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponse criarTokens(Usuario user) {
        return new LoginResponse(criarTokenJwt(user), EXPIRES_IN, criarRefreshTokenJwt(user, refreshTokenService.criar(user).getToken()));
    }

    public DadosToken criarTokensOauth(Usuario user) {
        return new DadosToken(criarTokenJwt(user), criarRefreshTokenJwt(user, refreshTokenService.criar(user).getToken()));
    }

    private String criarTokenJwt(Usuario usuario) {
        var claims = criarClaims(usuario);
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private JwtClaimsSet criarClaims(Usuario user) {
        var now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer("GLAISS")
                .subject(UserSubjectDto.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .build()
                        .toString())
                .expiresAt(expiracao())
                .issuedAt(now)
                .claim("authorities", user.getPrivilegio())
                .build();
    }

    private String criarRefreshTokenJwt(Usuario usuario, String token) {
        var claims = criarClaimsRefreshToken(usuario, token);
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private JwtClaimsSet criarClaimsRefreshToken(Usuario user, String token) {
        var now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer("GLAISS")
                .subject(UserSubjectRefreshTokenDto.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .refreshToken(token)
                        .build()
                        .toString())
                .expiresAt(expiracao())
                .issuedAt(now)
                .claim("authorities", user.getPrivilegio())
                .build();
    }

    private Instant expiracao() {
        return LocalDateTime.now().plusMinutes(TokenService.EXPIRES_IN).toInstant(ZoneOffset.of("-03:00"));
    }

    public LoginResponse renovarToken(RefreshToken refreshToken, Usuario usuario) {
        try {
            return new LoginResponse(criarTokenJwt(usuario), EXPIRES_IN, criarRefreshTokenJwt(usuario, refreshToken.getToken()));
        } catch (Exception e) {
            throw new CredencialException();
        }
    }

    public RefreshToken validarRefreshToken(String refreshToken) {
        try {
            return refreshTokenService.validar(refreshToken);
        } catch (Exception e) {
            throw new CredencialException();
        }
    }
}
