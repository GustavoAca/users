package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.CredencialException;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.controller.dto.LoginRequest;
import com.glaiss.users.controller.dto.LoginResponse;
import com.glaiss.users.domain.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenComponent {

    private final UsuarioService usuarioService;

    private final static Long EXPIRES_IN = 3050L;

    public TokenComponent(
            UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

    }

    public LoginResponse login(LoginRequest loginRequest) {
//        Usuario user = usuarioService.findByUsername(loginRequest.username())
//                .orElseThrow(() -> new RegistroNaoEncontradoException("Usuario não encontrado"));
//
//        validar(user, loginRequest);
//
//        var claims = criarClaims(user);
//
//        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponse("jwtValue", EXPIRES_IN);
    }

//    private JwtClaimsSet criarClaims(Usuario user) {
//        var now = Instant.now();
//        return JwtClaimsSet.builder()
//                .issuer("GLAISS")
//                .subject(UserSubjectDto.builder()
//                        .userId(user.getId())
//                        .username(user.getUsername())
//                        .build()
//                        .toString())
//                .expiresAt(now.plusSeconds(EXPIRES_IN))
//                .issuedAt(now)
//                .claim("authorities", user.getPrivilegio())
//                .build();
//    }

    private void validar(Usuario user, LoginRequest loginRequest) {
        if (!user.isLoginCorretc(loginRequest)) {
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
