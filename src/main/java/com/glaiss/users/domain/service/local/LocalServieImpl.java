package com.glaiss.users.domain.service.local;

import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.domain.model.Local;
import com.glaiss.users.domain.model.dto.LocalDto;
import com.glaiss.users.domain.model.mapper.LocalMapper;
import com.glaiss.users.domain.repository.LocalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocalServieImpl extends BaseServiceImpl<Local, UUID, LocalRepository> implements LocalService {

    private final LocalMapper localMapper;
    protected LocalServieImpl(LocalRepository repo,
                              LocalMapper localMapper) {
        super(repo);
        this.localMapper = localMapper;
    }

    public LocalDto salvar(LocalDto localDto){
        return localMapper.toDto(super.salvar(localMapper.toEntity(localDto)));
    }

    @Override
    public Page<LocalDto> listarPaginaDto(Pageable pageable) {
        var locaisPage = super.listarPagina(pageable);
        var locais = locaisPage.getContent().stream()
                .map(localMapper::toDto).toList();
        return new PageImpl<>(locais, pageable, locaisPage.getTotalElements());
    }

    @Override
    public LocalDto buscarPorIdDto(UUID id) {
        return localMapper.toDto(buscarPorId(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException(id, "Local")));
    }
}
