package com.glaiss.users.controller;

import com.glaiss.core.domain.model.ResponsePage;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import com.glaiss.users.domain.service.listascompra.ListaCompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/lista-compra")
public class ListaCompraController {

    private final ListaCompraService listaCompraService;

    @Autowired
    public ListaCompraController(ListaCompraService listaCompraService) {
        this.listaCompraService = listaCompraService;
    }

    @PostMapping
    @CacheEvict(value = "ListaCompra", allEntries = true)
    public ListaCompraDto cadastrar() {
        return listaCompraService.salvar();
    }

    @GetMapping
    @Cacheable(value = "ListaCompra", key = "#pageable.pageNumber")
    public ResponsePage<ListaCompraDto> listar(@PageableDefault(size = 20) Pageable pageable) {
        return listaCompraService.listarPaginaDto(pageable);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "ListaCompra", key = "#id")
    public ListaCompraDto buscarPorId(@PathVariable UUID id) {
        return listaCompraService.buscarPorIdDto(id);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "ListaCompra", allEntries = true)
    public Boolean deletar(@PathVariable UUID id) {
        return listaCompraService.deletar(id);
    }

    @PutMapping("/atualizar-valor-total")
    @CacheEvict(value = "ListaCompra", allEntries = true)
    public ListaCompraDto atualizarValorTotal(@Valid @RequestBody ListaCompraDto listaCompraDto) {
        return listaCompraService.atualizarValorTotal(listaCompraDto);
    }
}
