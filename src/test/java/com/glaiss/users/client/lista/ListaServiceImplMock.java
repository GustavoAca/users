package com.glaiss.users.client.lista;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ListaServiceImplMock implements ListaService {
    @Override
    public Boolean deletar(UUID id) {
        return Boolean.TRUE;
    }
}
