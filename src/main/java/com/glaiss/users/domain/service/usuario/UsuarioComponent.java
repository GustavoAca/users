package com.glaiss.users.domain.service.usuario;

import com.glaiss.core.exception.CredencialException;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.core.security.Privilegio;
import com.glaiss.users.controller.dto.*;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.model.dto.UserSubjectDto;
import com.glaiss.users.infra.client.oauth.DadosOauth;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
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
        Usuario user = buscarUsuario(loginRequest.username());
        validar(user, loginRequest);
        return new LoginResponse(criarTokenJwt(user), EXPIRES_IN, criarRefreshTokenJwt(user));
    }

    public DadosToken loginOauth(DadosOauth dadosOauth) {
        Usuario user = buscarUsuarioSeNaoExistirCriar(dadosOauth);
        return new DadosToken(criarTokenJwt(user), criarRefreshTokenJwt(user));
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
                .expiresAt(now.plusSeconds(EXPIRES_IN))
                .issuedAt(now)
                .claim("authorities", user.getPrivilegio())
                .build();
    }

    private String criarRefreshTokenJwt(Usuario usuario) {
        var now = Instant.now();
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                        .issuer("GLAISS")
                        .subject(UserSubjectDto.builder()
                                .userId(usuario.getId())
                                .username(usuario.getUsername())
                                .build()
                                .toString())
                        .expiresAt(now.plusSeconds(EXPIRES_IN * 2))
                        .issuedAt(now)
                        .claim("authorities", usuario.getPrivilegio())
                        .build()
        )).getTokenValue();
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
            Usuario user = buscarUsuario(alterarUserDto.username());
            user.setPassword(bCryptPasswordEncoder.encode(alterarUserDto.senha()));
            usuarioService.salvar(user);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public Usuario buscarUsuario(String username) {
        return usuarioService.findByUsername(username)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Usuario não encontrado"));
    }

    public Usuario buscarUsuarioSeNaoExistirCriar(DadosOauth dadosOauth) {
       Optional<Usuario> usuario = usuarioService.findByUsername(dadosOauth.email());
       if(usuario.isPresent()){
           return usuario.get();
       } else {
           Usuario novoUsuario = Usuario.builder()
                   .username(dadosOauth.email())
                   .nome(dadosOauth.name())
                   .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                   .privilegio(Privilegio.ROLE_FREE)
                   .isAtivo(Boolean.TRUE)
                   .build();
           return usuarioService.salvar(novoUsuario);
       }
    }
}
