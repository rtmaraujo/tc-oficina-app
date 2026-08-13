package br.com.fiap.presentation.controller;

import br.com.fiap.application.service.MetricasOrdemServicoService;
import br.com.fiap.application.service.MetricasOrdemServicoService.EstatisticasExecucaoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/metricas")
@Slf4j
public class MetricasController {
    private final MetricasOrdemServicoService metricasService;

    public MetricasController(MetricasOrdemServicoService metricasService) {
        this.metricasService = metricasService;
    }

    @GetMapping("/tempo-execucao")
    public ResponseEntity<EstatisticasExecucaoDTO> obterTempoMedioExecucao() {
        log.info("Consultando estatísticas de tempo de execução das ordens de serviço");
        EstatisticasExecucaoDTO estatisticas = metricasService.obterEstatisticas();
        log.info(
                "Estatísticas calculadas - total: {}, médio: {}, min: {}, max: {}",
                estatisticas.totalOrdensFinalizadas(),
                estatisticas.tempoMedioEmMinutos(),
                estatisticas.tempoMinimoEmMinutos(),
                estatisticas.tempoMaximoEmMinutos()
        );
        return ResponseEntity.ok(estatisticas);
    }
}