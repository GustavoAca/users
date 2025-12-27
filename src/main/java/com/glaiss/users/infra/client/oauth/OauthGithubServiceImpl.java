package com.glaiss.users.infra.client.oauth;

import com.glaiss.core.exception.RegistroNaoEncontradoException;
import com.glaiss.users.controller.dto.DadosToken;
import com.glaiss.users.domain.service.usuario.UsuarioComponent;
import com.glaiss.users.infra.client.dto.GithubAccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

@Service
public class OauthGithubServiceImpl implements OauthService {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestTemplate restTemplate;
    private final UsuarioComponent usuarioComponent;

    @Autowired
    public OauthGithubServiceImpl(RestTemplate restTemplate,
                                  UsuarioComponent usuarioComponent,
                                  @Value("${github.oauth.client.id}") String clientId,
                                  @Value("${github.oauth.client.secret}") String clientSecret,
                                  @Value("${github.oauth.redirect-uri:http://localhost:8180/users/oauth/github/autorizado}") String redirectUri) {
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
        return "https://github.com/login/oauth/authorize"+
                "?client_id="+clientId +
                "&redirect_uri="+redirectUri +
                "&scope=read:user,user:email,public_repo";
    }

    @Override
    public DadosToken autenticar(String code) {
        var dadosOauth = obterDadosOauth(code);
        return usuarioComponent.loginOauth(dadosOauth);
    }

    public DadosOauth obterDadosOauth(String code){
        var token = obterToken(code);

        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        var entity = new HttpEntity<>(headers);

        ResponseEntity<Map> respostaPerfil = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                entity,
                Map.class
        );
        String nome = (String) respostaPerfil.getBody().get("name");

        ResponseEntity<DadosEmail[]> resposta = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                DadosEmail[].class
        );

        var body = resposta.getBody();

        if(Objects.isNull(body) || body.length == 0){
            throw new RegistroNaoEncontradoException("Email do usuário não encontrado no GitHub");
        }

        for(DadosEmail d: body){
            if(d.primary() && d.verified())
                return new DadosOauth(d.email(), nome);
        }

        return null;
    }

    private String obterToken(String code) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        var body = Map.of(
                "code", code,
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri
        );

        var request = new HttpEntity<>(body, headers);

        ResponseEntity<GithubAccessTokenResponse> resposta = restTemplate.postForEntity(
                "https://github.com/login/oauth/access_token",
                request,
                GithubAccessTokenResponse.class
        );

        GithubAccessTokenResponse tokenResponse = resposta.getBody();
        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new RegistroNaoEncontradoException("Token de acesso não obtido do GitHub");
        }
        return tokenResponse.getAccessToken();
    }
}
