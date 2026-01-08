package com.glaiss.users.config;

import com.glaiss.core.utils.SecurityContextUtils;
import com.glaiss.users.domain.model.Usuario;
import com.glaiss.users.domain.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepositoy;

    public FiltroTokenAcesso(UsuarioRepository usuarioRepositoy) {
        this.usuarioRepositoy = usuarioRepositoy;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperarTokenRequisicao(request);

        if(token != null){
            if(!SecurityContextUtils.validateToken(token)){
                return;
            }

            String email = SecurityContextUtils.getEmailFromToken(token);

            Usuario usuario = usuarioRepositoy.findByUsername(email).orElseThrow();

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getPrivilegio());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
