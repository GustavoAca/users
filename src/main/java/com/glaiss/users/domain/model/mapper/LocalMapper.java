package com.glaiss.users.domain.model.mapper;

import com.glaiss.users.domain.model.Local;
import com.glaiss.users.domain.model.dto.LocalDto;

public class LocalMapper {

    public LocalDto toDto(Local local){
        return LocalDto.builder()
                .id(local.getId())
                .nome(local.getNome())
                .endereco(local.getEndereco())
                .cep(local.getCep())
                .logradouro(local.getLogradouro())
                .bairro(local.getBairro())
                .numero(local.getNumero())
                .estado(local.getEstado())
                .build();
    }

    public Local toEntity(LocalDto dto){
        return Local.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .endereco(dto.getEndereco())
                .cep(dto.getCep())
                .logradouro(dto.getLogradouro())
                .bairro(dto.getBairro())
                .numero(dto.getNumero())
                .estado(dto.getEstado())
                .build();
    }
}
