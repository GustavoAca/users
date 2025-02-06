package com.glaiss.users.domain.model.dto;

import com.glaiss.core.domain.model.dto.EntityAbstractDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ListaCompraDto extends EntityAbstractDto {

    private UUID id;
    private UUID usuarioId;
    @Builder.Default
    private BigDecimal valorTotal = BigDecimal.ZERO;
}
