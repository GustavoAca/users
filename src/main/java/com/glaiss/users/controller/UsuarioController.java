package com.glaiss.users.controller;

import com.glaiss.users.controller.dto.*;
import com.glaiss.users.domain.service.usuario.UsuarioComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioComponent usuarioComponent;

    @Autowired
    public UsuarioController(UsuarioComponent usuarioComponent) {
        this.usuarioComponent = usuarioComponent;
    }

    @PostMapping("/cadastrar")
    public void cadastrar(@RequestBody CreateUserDto createUserDto) {
        usuarioComponent.cadastrarUsuario(createUserDto);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return usuarioComponent.login(loginRequest);
    }

    @DeleteMapping("/{id}")
    public Boolean deletar(@PathVariable UUID id) {
        return usuarioComponent.deletarUsuario(id);
    }

    @PutMapping("/alterar-senha")
    public Boolean alterar(@RequestBody AlterarUserDto alterarUserDto) {
        return usuarioComponent.alterarSenha(alterarUserDto);
    }

    @PostMapping("/atualizar-token")
    public LoginResponse renovarToken(@RequestBody RefreshTokenDto refreshToken) {
        return usuarioComponent.renovarToken(refreshToken);
    }
}
