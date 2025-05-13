package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.CredencialException;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.controller.dto.AlterarUserDto;
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
public class UsuarioComponent {

    private final JwtEncoder jwtEncoder;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static Long EXPIRES_IN = 3050L;

    public UsuarioComponent(JwtEncoder jwtEncoder,
                            UsuarioService usuarioService,
                            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioService = usuarioService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Usuario user = findUsuario(loginRequest.username());
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

    public Boolean deletarUsuario(UUID id) {
        return usuarioService.deletar(id);
    }

    public Boolean alterarSenha(AlterarUserDto alterarUserDto) {
        try {
            Usuario user = findUsuario(alterarUserDto.username());
            user.setPassword(bCryptPasswordEncoder.encode(alterarUserDto.senha()));
            usuarioService.salvar(user);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private Usuario findUsuario(String username) {
        return usuarioService.findByUsername(username)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Usuario não encontrado"));

    }
}
