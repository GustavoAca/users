package com.glaiss.users.domain.service.listascompra;

import com.glaiss.core.domain.model.ResponsePage;
import com.glaiss.core.domain.service.BaseService;
import com.glaiss.users.client.lista.ItemListaDto;
import com.glaiss.users.domain.model.ListaCompra;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ListaCompraService extends BaseService<ListaCompra, UUID> {

    ListaCompraDto salvar();

    ResponsePage<ListaCompraDto> listarPaginaDto(Pageable pageable);

    ListaCompraDto buscarPorIdDto(UUID id);

    void removerItemLista(List<ItemListaDto> itensLista);

    ListaCompraDto atualizarValorTotal(ListaCompraDto listaCompraDto);
}
