package br.com.fiap.presentation.controller;

import br.com.fiap.infrastructure.config.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AutenticacaoController {

    private final JwtUtil jwtUtil;

    @Value("${admin.usuario}")
    private String adminUsuario;

    @Value("${admin.senha}")
    private String adminSenha;

    public AutenticacaoController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody UsuarioRequest request) {
        log.info("Tentativa de login para usuário: {}", request.usuario());
        if (adminUsuario.equals(request.usuario()) && adminSenha.equals(request.senha())) {
            String token = jwtUtil.generateToken(request.usuario());
            log.info("Login realizado com sucesso para usuário: {}", request.usuario());
            return ResponseEntity.ok(new UsuarioResponse(token, "Bearer", 86400));
        }
        log.warn("Falha no login para usuário: {}", request.usuario());
        return ResponseEntity.status(401).build();
    }

    public record UsuarioRequest(String usuario, String senha) {}
    public record UsuarioResponse(String access_token, String token_type, long expires_in) {}
}
