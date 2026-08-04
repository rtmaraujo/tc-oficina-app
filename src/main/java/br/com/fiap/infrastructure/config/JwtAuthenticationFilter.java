package br.com.fiap.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String path = request.getRequestURI();
        log.debug("Processando requisicao: {} | Authorization header present: {}", path, header != null);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            boolean valid = jwtUtil.validateToken(token);
            log.debug("Token validation result: {}", valid);

            if (valid) {
                String username = jwtUtil.extractUsername(token);
                log.debug("Token valido para usuario: {}", username);
                UserDetails userDetails = User.withUsername(username).password("").authorities(Collections.emptyList()).build();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Token JWT invalido para requisicao: {}", path);
            }
        } else if (path.startsWith("/api/v1/admin/")) {
            log.warn("Requisicao para endpoint admin sem token: {}", path);
        }

        filterChain.doFilter(request, response);
    }
}
