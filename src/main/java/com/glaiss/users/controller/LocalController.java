package com.glaiss.users.controller;

import com.glaiss.users.domain.model.dto.LocalDto;
import com.glaiss.users.domain.service.local.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/locais")
public class LocalController {

    private final LocalService localService;

    @Autowired
    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @PostMapping
    public ResponseEntity<LocalDto> cadastrar(@RequestBody LocalDto localDto) {
        return ResponseEntity.ok(localService.salvar(localDto));
    }

    @CacheEvict(value = "Local", key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletar(@PathVariable UUID id) {
        return ResponseEntity.ok(localService.deletar(id));
    }

    @GetMapping
    @Cacheable(value = "Local", key = "#pageable.pageNumber")
    public ResponseEntity<Page<LocalDto>> listar(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(localService.listarPaginaDto(pageable));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "Local", key = "#id")
    public ResponseEntity<LocalDto> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(localService.buscarPorIdDto(id));
    }
}
