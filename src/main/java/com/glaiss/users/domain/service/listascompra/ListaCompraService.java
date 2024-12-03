package com.glaiss.users.domain.service.listascompra;

import com.glaiss.core.domain.service.BaseService;
import com.glaiss.users.domain.model.ListaCompra;
import com.glaiss.users.domain.model.dto.ListaCompraDto;

import java.util.UUID;

public interface ListaCompraService extends BaseService<ListaCompra, UUID> {

    ListaCompraDto salvar(ListaCompraDto listaCompraDto);
}
