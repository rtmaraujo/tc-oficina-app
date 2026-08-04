package br.com.fiap.application.service;

import br.com.fiap.domain.model.*;
import br.com.fiap.domain.repository.OrdemServicoRepository;
import br.com.fiap.domain.service.OrdemServicoDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MetricasOrdemServicoServiceTest {

    private OrdemServicoRepository repository;
    private MetricasOrdemServicoService service;

    @BeforeEach
    void setup() {
        repository = mock(OrdemServicoRepository.class);
        service = new MetricasOrdemServicoService(repository);
    }

    private OrdemServico criarOrdem(int minutos) {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(minutos);

        // Objetos mínimos necessários
        Cliente cliente = mock(Cliente.class);
        Veiculo veiculo = mock(Veiculo.class);
        Servico servico = new Servico("Troca de Óleo Premium", "Óleo sintético de qualidade", new BigDecimal("200.00"));
        Peca peca = new Peca("Óleo Sintético", "5L", new BigDecimal("85.50"), 20);

        OrdemServicoDomainService ordemServicoDomainService = new OrdemServicoDomainService();

        OrdemServico ordem = new OrdemServico(
                cliente,
                veiculo,
                List.of(servico),
                List.of(peca), inicio.minusMinutes(minutos)
        );
        ordemServicoDomainService.avancaStatus(ordem);
        ordem.atualizaStatus(OrdemServicoStatus.ENTREGUE);
        return ordem;
    }

    @Test
    void deveCalcularTempoMedioCorretamente() {
        when(repository.findAll()).thenReturn(List.of(
                criarOrdem(10),
                criarOrdem(20),
                criarOrdem(30)
        ));

        double media = service.calcularTempoMedioExecucaoEmMinutos();

        assertEquals(20.0, media);
    }

    @Test
    void deveRetornarZeroQuandoNaoHaOrdensFinalizadas() {
        when(repository.findAll()).thenReturn(List.of());

        double media = service.calcularTempoMedioExecucaoEmMinutos();

        assertEquals(0.0, media);
    }

    @Test
    void deveCalcularTempoMinimo() {
        when(repository.findAll()).thenReturn(List.of(
                criarOrdem(15),
                criarOrdem(5),
                criarOrdem(25)
        ));

        long min = service.calcularTempoMinimoEmMinutos();

        assertEquals(5, min);
    }

    @Test
    void deveCalcularTempoMaximo() {
        when(repository.findAll()).thenReturn(List.of(
                criarOrdem(15),
                criarOrdem(5),
                criarOrdem(25)
        ));

        long max = service.calcularTempoMaximoEmMinutos();

        assertEquals(25, max);
    }

    @Test
    void deveContarOrdensFinalizadas() {
        when(repository.findAll()).thenReturn(List.of(
                criarOrdem(10),
                criarOrdem(20)
        ));

        long total = service.contarOrdensFinalizadas();

        assertEquals(2, total);
    }

    @Test
    void deveRetornarEstatisticasCompletas() {
        when(repository.findAll()).thenReturn(List.of(
                criarOrdem(10),
                criarOrdem(20),
                criarOrdem(30)
        ));

        var stats = service.obterEstatisticas();

        assertEquals(3, stats.totalOrdensFinalizadas());
        assertEquals(20.0, stats.tempoMedioEmMinutos());
        assertEquals(10, stats.tempoMinimoEmMinutos());
        assertEquals(30, stats.tempoMaximoEmMinutos());
    }

    @Test
    void deveRetornarEstatisticasZeradasQuandoNaoHaDados() {
        when(repository.findAll()).thenReturn(List.of());

        var stats = service.obterEstatisticas();

        assertEquals(0, stats.totalOrdensFinalizadas());
        assertEquals(0.0, stats.tempoMedioEmMinutos());
        assertEquals(0, stats.tempoMinimoEmMinutos());
        assertEquals(0, stats.tempoMaximoEmMinutos());
    }
}