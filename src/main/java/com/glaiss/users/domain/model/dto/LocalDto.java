package com.glaiss.users.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LocalDto {

    private UUID id;
    private String nome;
    private String endereco;
    private String cep;
    private String logradouro;
    private String bairro;
    private String numero;
    private String estado;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
}
