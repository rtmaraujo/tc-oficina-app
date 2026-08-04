package br.com.fiap.application.service;

import br.com.fiap.domain.model.OrdemServico;
import br.com.fiap.domain.model.OrdemServicoStatus;
import br.com.fiap.domain.repository.OrdemServicoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class MetricasOrdemServicoService {
    private final OrdemServicoRepository ordemServicoRepository;

    public MetricasOrdemServicoService(OrdemServicoRepository ordemServicoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
    }

    public double calcularTempoMedioExecucaoEmMinutos() {
        log.info("Calculando estatísticas de execução das ordens de serviço");
        List<OrdemServico> ordensFinalizadas = ordemServicoRepository.findAll().stream()
                .filter(ordem -> ordem.getStatus() == OrdemServicoStatus.ENTREGUE && ordem.getFinalizadoEm() != null)
                .toList();
        if (ordensFinalizadas.isEmpty()) {
            return 0.0;
        }
        long totalMinutos = ordensFinalizadas.stream()
                .mapToLong(ordem -> ChronoUnit.MINUTES.between(ordem.getCriadoEm(), ordem.getFinalizadoEm()))
                .sum();
        return (double) totalMinutos / ordensFinalizadas.size();
    }

    public long calcularTempoMinimoEmMinutos() {
        return ordemServicoRepository.findAll().stream()
                .filter(ordem -> ordem.getStatus() == OrdemServicoStatus.ENTREGUE && ordem.getFinalizadoEm() != null)
                .mapToLong(ordem -> ChronoUnit.MINUTES.between(ordem.getCriadoEm(), ordem.getFinalizadoEm()))
                .min()
                .orElse(0);
    }

    public long calcularTempoMaximoEmMinutos() {
        return ordemServicoRepository.findAll().stream()
                .filter(ordem -> ordem.getStatus() == OrdemServicoStatus.ENTREGUE && ordem.getFinalizadoEm() != null)
                .mapToLong(ordem -> ChronoUnit.MINUTES.between(ordem.getCriadoEm(), ordem.getFinalizadoEm()))
                .max()
                .orElse(0);
    }

    public long contarOrdensFinalizadas() {
        return ordemServicoRepository.findAll().stream()
                .filter(ordem -> ordem.getStatus() == OrdemServicoStatus.ENTREGUE && ordem.getFinalizadoEm() != null)
                .count();
    }

    public EstatisticasExecucaoDTO obterEstatisticas() {
        long totalFinalizadas = contarOrdensFinalizadas();
        if (totalFinalizadas == 0) {
            return new EstatisticasExecucaoDTO(0, 0.0, 0, 0);
        }
        return new EstatisticasExecucaoDTO(
                totalFinalizadas,
                calcularTempoMedioExecucaoEmMinutos(),
                calcularTempoMinimoEmMinutos(),
                calcularTempoMaximoEmMinutos()
        );
    }

    public record EstatisticasExecucaoDTO(long totalOrdensFinalizadas, double tempoMedioEmMinutos,
                                          long tempoMinimoEmMinutos, long tempoMaximoEmMinutos) {}
}