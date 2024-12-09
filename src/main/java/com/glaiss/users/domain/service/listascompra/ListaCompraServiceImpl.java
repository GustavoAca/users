package com.glaiss.users.domain.service.listascompra;

import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.users.domain.mapper.ListaCompraMapper;
import com.glaiss.users.domain.model.ListaCompra;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import com.glaiss.users.domain.repository.listascompra.ListaCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ListaCompraServiceImpl extends BaseServiceImpl<ListaCompra, UUID, ListaCompraRepository> implements ListaCompraService {

    private final ListaCompraMapper listaCompraMapper;

    @Autowired
    protected ListaCompraServiceImpl(ListaCompraRepository repo,
                                     ListaCompraMapper listaCompraMapper) {
        super(repo);
        this.listaCompraMapper = listaCompraMapper;
    }

    @Override
    public ListaCompraDto salvar(ListaCompraDto listaCompraDto) {
        return listaCompraMapper.toDto(salvar(listaCompraMapper.toEntity(listaCompraDto)));
    }
}
