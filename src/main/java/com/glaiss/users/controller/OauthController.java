package com.glaiss.users.controller;

import com.glaiss.users.controller.dto.DadosToken;
import com.glaiss.users.infra.client.oauth.TipoOauth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @GetMapping("/github")
    public ResponseEntity<Void> redirecionarGithub(){
        return new ResponseEntity<>(TipoOauth.GITHUB.getService().redirecionarParaOauth(), HttpStatus.FOUND);
    }

    @GetMapping("/github/autorizado")
    public ResponseEntity<DadosToken> autenticarUsuarioOAuthGithub(@RequestParam String code){
        return ResponseEntity.ok(TipoOauth.GITHUB.getService().autenticar(code));
    }

    @GetMapping("/google")
    public ResponseEntity<Void> redirecionarGoogle(){
        return new ResponseEntity<>(TipoOauth.GOOGLE.getService().redirecionarParaOauth(), HttpStatus.FOUND);
    }

    @GetMapping("/google/autorizado")
    public ResponseEntity<DadosToken> autenticarUsuarioOAuthGoogle(@RequestParam String code){
        return ResponseEntity.ok(TipoOauth.GOOGLE.getService().autenticar(code));
    }
}
