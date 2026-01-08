package com.glaiss.users.domain.service.usuario;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.glaiss.core.exception.GlaissException;
import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.controller.dto.DadosToken;
import com.glaiss.users.controller.dto.LoginResponse;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.model.dto.UserSubjectDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Service
public class TokenService {

    @Value("${secret-key}")
    private String secretKey;

    private final static Integer EXPIRES_IN = 60;

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse criarTokens(Usuario user) {
        return new LoginResponse(criarTokenJwt(user, EXPIRES_IN), EXPIRES_IN, criarTokenJwt(user, EXPIRES_IN * 2));
    }

    public DadosToken criarTokensOauth(Usuario user) {
        return new DadosToken(criarTokenJwt(user, EXPIRES_IN), criarTokenJwt(user, EXPIRES_IN * 2));
    }

    private String criarTokenJwt(Usuario usuario, Integer tempoExpiracao) {
        var now = Instant.now();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return
                JWT.create()
                        .withIssuer("GLAISS")
                        .withSubject(UserSubjectDto.builder()
                                .userId(usuario.getId())
                                .username(usuario.getUsername())
                                .build()
                                .toString())
                        .withExpiresAt(expiracao(tempoExpiracao))
                        .withIssuedAt(now)
                        .withClaim("authorities", usuario.getPrivilegio().name())
                        .sign(algorithm);
    }

    private Instant expiracao(Integer minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }

    public Map<String, String> verificarToken(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("GLAISS")
                    .build();
            decodedJWT = verifier.verify(token);
            String object = decodedJWT.getSubject();
            var informacaoToken = object.split(",");
            return Map.of(
                    "userId", informacaoToken[0].split(":")[1],
                    "username", informacaoToken[1].split(":")[1].replace("}", "")
            );
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Erro ao verificar token JWT de acesso!");
        }
    }
}
