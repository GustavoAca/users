package com.glaiss.users.client.lista;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ListaServiceImplMock implements ListaService {
    @Override
    public ResponseEntity<Boolean> deletar(UUID id) {
        return ResponseEntity.ok(Boolean.TRUE);
    }
}
