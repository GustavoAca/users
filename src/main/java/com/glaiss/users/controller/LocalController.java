package com.glaiss.users.controller;

import com.glaiss.users.domain.model.dto.LocalDto;
import com.glaiss.users.domain.service.local.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @CacheEvict(value = "Local", allEntries = true)
    public LocalDto cadastrar(@RequestBody LocalDto localDto) {
        return localService.salvar(localDto);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "Local", allEntries = true)
    public Boolean deletar(@PathVariable UUID id) {
        return localService.deletar(id);
    }

    @GetMapping
    @Cacheable(value = "Local", key = "#pageable.pageNumber")
    public Page<LocalDto> listar(@PageableDefault(size = 20) Pageable pageable) {
        return localService.listarPaginaDto(pageable);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "Local", key = "#id")
    public LocalDto buscarPorId(@PathVariable("id") UUID id) {
        return localService.buscarPorIdDto(id);
    }
}
