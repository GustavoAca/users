package com.glaiss.users.domain.service.local;

import com.glaiss.core.domain.model.ResponsePage;
import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.domain.mapper.LocalMapper;
import com.glaiss.users.domain.model.Local;
import com.glaiss.users.domain.model.dto.LocalDto;
import com.glaiss.users.domain.repository.LocalRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocalServiceImpl extends BaseServiceImpl<Local, UUID, LocalRepository> implements LocalService {

    private final LocalMapper localMapper;

    protected LocalServiceImpl(LocalRepository repo,
                               LocalMapper localMapper) {
        super(repo);
        this.localMapper = localMapper;
    }

    public LocalDto salvar(LocalDto localDto){
        return localMapper.toDto(salvar(localMapper.toEntity(localDto)));
    }

    @Override
    public ResponsePage<LocalDto> listarPaginaDto(Pageable pageable) {
        var locaisPage = listarPagina(pageable);
        var locais = locaisPage.getContent().stream()
                .map(localMapper::toDto).toList();
        return new ResponsePage<>(locais, pageable.getPageNumber(), pageable.getPageSize(), locaisPage.getTotalElements());
    }

    @Override
    public LocalDto buscarPorIdDto(UUID id) {
        return localMapper.toDto(buscarPorId(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException(id, "Local")));
    }
}
