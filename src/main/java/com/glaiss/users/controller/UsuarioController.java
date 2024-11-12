package com.glaiss.users.controller;

import com.glaiss.users.controller.dto.CreateUserDto;
import com.glaiss.users.domain.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody CreateUserDto createUserDto) {
        usuarioService.cadastrarUsuario(createUserDto);
        return ResponseEntity.ok().build();
    }
}
