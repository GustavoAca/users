package com.glaiss.users.domain.model.dto;

import com.glaiss.core.domain.model.dto.EntityAbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ListaCompraDto extends EntityAbstractDto {

    private UUID id;
    private UUID usuarioId;
}
