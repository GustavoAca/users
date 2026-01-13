package com.glaiss.users.infra.client.oauth;

import com.auth0.jwt.JWT;
import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.controller.dto.DadosToken;
import com.glaiss.users.domain.service.usuario.UsuarioComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class OauthGoogleServiceImpl implements OauthService {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestTemplate restTemplate;
    private final UsuarioComponent usuarioComponent;

    @Autowired
    public OauthGoogleServiceImpl(RestTemplate restTemplate,
                                  UsuarioComponent usuarioComponent,
                                  @Value("${google.oauth.client.id}") String clientId,
                                  @Value("${google.oauth.client.secret}") String clientSecret,
                                  @Value("${google.oauth.client.redirect-uri}") String redirectUri) {
        this.restTemplate = restTemplate;
        this.usuarioComponent = usuarioComponent;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
    @Override
    public HttpHeaders redirecionarParaOauth() {
        var headers = new HttpHeaders();
        headers.setLocation(URI.create(gerarUrl()));
        return headers;
    }

    public String gerarUrl(){
        return "https://accounts.google.com/o/oauth2/v2/auth"+
                "?client_id="+clientId +
                "&redirect_uri="+redirectUri +
                "&scope=https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile"+
                "&response_type=code";
    }

    @Override
    public DadosToken autenticar(String code) {
        var dadosOauth = obterDadosOauth(code);
        return usuarioComponent.loginOauth(dadosOauth);
    }

    public DadosOauth obterDadosOauth(String code){
        var token = obterToken(code);
        var decodedJWT = JWT.decode(token);
        return new DadosOauth(decodedJWT.getClaim("email").asString(), decodedJWT.getClaim("name").asString());
    }

    private String obterToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        if (body == null || body.get("id_token") == null) {
            throw new RegistroNaoEncontradoException("Token não retornado pelo provedor OAuth");
        }
        return body.get("id_token").toString();
    }
}
