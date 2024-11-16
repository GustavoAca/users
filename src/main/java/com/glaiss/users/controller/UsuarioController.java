package com.glaiss.users.controller;

import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.controller.dto.LoginRequest;
import com.glaiss.users.controller.dto.LoginResponse;
import com.glaiss.users.domain.service.usuario.TokenComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody CreateUserDto createUserDto) {
        tokenComponent.cadastrarUsuario(createUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(tokenComponent.login(loginRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletar(@PathVariable UUID id) {
        return ResponseEntity.ok(tokenComponent.deletarUsuario(id));
    }
}
