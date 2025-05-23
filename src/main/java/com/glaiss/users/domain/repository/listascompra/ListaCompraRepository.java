package com.glaiss.users.domain.repository.listascompra;

import com.glaiss.core.domain.repository.BaseRepository;
import com.glaiss.users.domain.model.ListaCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ListaCompraRepository extends BaseRepository<ListaCompra, UUID> {

    Page<ListaCompra> findByUsuarioId(UUID usuarioId, Pageable pageable);
}
