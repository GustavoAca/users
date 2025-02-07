package com.glaiss.users.controller;

import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.controller.dto.LoginRequest;
import com.glaiss.users.controller.dto.LoginResponse;
import com.glaiss.users.domain.service.usuario.TokenComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final TokenComponent tokenComponent;

    @Autowired
    public UsuarioController(TokenComponent tokenComponent) {
        this.tokenComponent = tokenComponent;
    }

    @PostMapping("/cadastrar")
    public void cadastrar(@RequestBody CreateUserDto createUserDto) {
        tokenComponent.cadastrarUsuario(createUserDto);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return tokenComponent.login(loginRequest);
    }

    @DeleteMapping("/{id}")
    public Boolean deletar(@PathVariable UUID id) {
        return tokenComponent.deletarUsuario(id);
    }
}
