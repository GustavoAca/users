package com.glaiss.users.domain.service.listascompra;

import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.client.lista.ItemListaDto;
import com.glaiss.users.client.lista.ListaService;
import com.glaiss.users.domain.mapper.ListaCompraMapper;
import com.glaiss.users.domain.model.ListaCompra;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import com.glaiss.users.domain.repository.listascompra.ListaCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListaCompraServiceImpl extends BaseServiceImpl<ListaCompra, UUID, ListaCompraRepository> implements ListaCompraService {

    private final ListaCompraMapper listaCompraMapper;
    private final ListaService listaService;

    @Autowired
    protected ListaCompraServiceImpl(ListaCompraRepository repo,
                                     ListaCompraMapper listaCompraMapper,
                                     ListaService listaService) {
        super(repo);
        this.listaCompraMapper = listaCompraMapper;
        this.listaService = listaService;
    }

    @Override
    public ListaCompraDto salvar(ListaCompraDto listaCompraDto) {
        listaCompraDto.setUsuarioId(SecurityContextUtils.getId());
        return listaCompraMapper.toDto(salvar(listaCompraMapper.toEntity(listaCompraDto)));
    }

    @Override
    public Page<ListaCompraDto> listarPaginaDto(Pageable pageable) {
        var listaCompraPage = listarPagina(pageable);
        var listaCompra = listaCompraPage.getContent().stream()
                .map(listaCompraMapper::toDto).toList();
        return new PageImpl<>(listaCompra, pageable, listaCompraPage.getTotalElements());
    }

    @Override
    public ListaCompraDto buscarPorIdDto(UUID id) {
        return listaCompraMapper.toDto(buscarPorId(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException(id, "Lista de compra")));
    }

    @Override
    public void removerItemLista(List<ItemListaDto> itensLista) {
        itensLista.forEach(i -> listaService.deletar(i.id()));
    }
}
