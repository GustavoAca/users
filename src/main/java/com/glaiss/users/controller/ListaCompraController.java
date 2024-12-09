package com.glaiss.users.controller;

import com.glaiss.users.client.lista.ItemListaDto;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import com.glaiss.users.domain.service.listascompra.ListaCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<ListaCompraDto> cadastrar(@RequestBody ListaCompraDto listaCompraDto) {
        return ResponseEntity.ok(listaCompraService.salvar(listaCompraDto));
    }

    @GetMapping
    @Cacheable(value = "ListaCompra", key = "#pageable.pageNumber")
    public ResponseEntity<Page<ListaCompraDto>> listar(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(listaCompraService.listarPaginaDto(pageable));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "ListaCompra", key = "#id")
    public ResponseEntity<ListaCompraDto> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(listaCompraService.buscarPorIdDto(id));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "ListaCompra", key = "#id")
    public ResponseEntity<Boolean> deletar(@PathVariable UUID id) {
        return ResponseEntity.ok(listaCompraService.deletar(id));
    }

    @DeleteMapping
    public void removerItemLista(List<ItemListaDto> itensLista){
        listaCompraService.removerItemLista(itensLista);
    }
}
