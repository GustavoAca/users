package com.glaiss.users.domain.mapper;

import com.glaiss.users.domain.model.ListaCompra;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import org.springframework.stereotype.Component;

@Component
public class ListaCompraMapper {

    public ListaCompra toEntity(ListaCompraDto dto) {
        return ListaCompra.builder()
                .id(dto.getId())
                .usuarioId(dto.getUsuarioId())
                .valorTotal(dto.getValorTotal())
                .createdBy(dto.getCreatedBy())
                .createdDate(dto.getCreatedDate())
                .modifiedBy(dto.getModifiedBy())
                .modifiedDate(dto.getModifiedDate())
                .build();
    }

    public ListaCompraDto toDto(ListaCompra entity) {
        return ListaCompraDto.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuarioId())
                .valorTotal(entity.getValorTotal())
                .createdBy(entity.getCreatedBy())
                .createdDate(entity.getCreatedDate())
                .modifiedBy(entity.getModifiedBy())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }
}
