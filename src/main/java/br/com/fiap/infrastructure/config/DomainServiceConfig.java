package br.com.fiap.infrastructure.config;

import br.com.fiap.domain.service.CalculoOrcamentoService;
import br.com.fiap.domain.service.InventarioService;
import br.com.fiap.domain.service.OrdemServicoDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public OrdemServicoDomainService ordemServicoDomainService() {
        return new OrdemServicoDomainService();
    }

    @Bean
    public CalculoOrcamentoService calculoOrcamentoService() {
        return new CalculoOrcamentoService();
    }

    @Bean
    public InventarioService inventarioService() {
        return new InventarioService();
    }
}
