package com.glaiss.users.domain.service.listascompra;

import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.UsersApplicationTest;
import com.glaiss.users.client.lista.ListaService;
import com.glaiss.users.domain.mapper.ListaCompraMapper;
import com.glaiss.users.domain.model.ListaCompra;
import com.glaiss.users.domain.model.dto.ListaCompraDto;
import com.glaiss.users.domain.repository.listascompra.ListaCompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ListaCompraServiceImplTest extends UsersApplicationTest {

    private ListaCompraService listaCompraService;

    @Mock
    private ListaCompraRepository listaCompraRepository;

    @Autowired
    private ListaCompraMapper listaCompraMapper;

    @Autowired
    private ListaService listaService;

    @BeforeEach
    void setup() {
        listaCompraService = new ListaCompraServiceImpl(listaCompraRepository, listaCompraMapper, listaService);
    }

    @Nested
    class Dado_um_usuario_iniciando_uma_lista extends UsersApplicationTest {

        @Nested
        class Quando_iniciar extends UsersApplicationTest {
            private ListaCompraDto listaCompraDto;

            @BeforeEach
            void setup() {
                given(listaCompraRepository.save(any())).willReturn(ListaCompra.builder()
                        .id(UUID.randomUUID())
                        .usuarioId(SecurityContextUtils.getId())
                        .valorTotal(BigDecimal.TEN)
                        .build());
                listaCompraDto = listaCompraService.salvar();
            }

            @Test
            void Entao_deve_ser_criada_com_sucesso() {
                assertNotNull(listaCompraDto);
            }
        }
    }
}