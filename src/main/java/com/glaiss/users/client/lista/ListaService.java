package com.glaiss.users.client.lista;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Validated
@FeignClient(name = "lista-svc", url = "${glaiss.services.lista.url}", primary = false)
public interface ListaService {

    @DeleteMapping("/itens-lista/{id}")
    Boolean deletar(@PathVariable UUID id);
}
