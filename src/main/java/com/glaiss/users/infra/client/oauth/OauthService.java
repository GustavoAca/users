package com.glaiss.users.infra.client.oauth;

import com.glaiss.users.controller.dto.DadosToken;
import org.springframework.http.HttpHeaders;

public interface OauthService {

    HttpHeaders redirecionarParaOauth();

    DadosToken autenticar(String code);
}
