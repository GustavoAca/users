package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.CredencialException;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.controller.dto.LoginRequest;
import com.glaiss.users.controller.dto.LoginResponse;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.model.dto.UserSubjectDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class TokenComponent {

    private final JwtEncoder jwtEncoder;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static Long EXPIRES_IN = 3050L;

    public TokenComponent(JwtEncoder jwtEncoder,
                          UsuarioService usuarioService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioService = usuarioService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Usuario user = usuarioService.findByUsername(loginRequest.username())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Usuario não encontrado"));

        validar(user, loginRequest);

        var claims = criarClaims(user);

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponse(jwtValue, EXPIRES_IN);
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
                .expiresAt(now.plusSeconds(EXPIRES_IN))
                .issuedAt(now)
                .claim("authorities", user.getPrivilegio())
                .build();
    }

    private void validar(Usuario user, LoginRequest loginRequest) {
        if (!user.isLoginCorretc(loginRequest.password(), bCryptPasswordEncoder)) {
            throw new CredencialException();
        }
    }

    public void cadastrarUsuario(CreateUserDto createUserDto) {
        usuarioService.cadastrarUsuario(createUserDto);
    }

    public Boolean deletarUsuario(UUID id){
        return usuarioService.deletar(id);
    }
}
