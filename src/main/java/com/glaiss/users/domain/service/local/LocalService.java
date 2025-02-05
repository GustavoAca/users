package com.glaiss.users.domain.service.local;

import com.glaiss.core.domain.model.ResponsePage;
import com.glaiss.core.domain.service.BaseService;
import com.glaiss.users.domain.model.Local;
import com.glaiss.users.domain.model.dto.LocalDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LocalService extends BaseService<Local, UUID> {
    LocalDto salvar(LocalDto localDto);

    ResponsePage<LocalDto> listarPaginaDto(Pageable pageable);

    LocalDto buscarPorIdDto(UUID id);

}
