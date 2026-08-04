package br.com.fiap.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdemServicoDTO(
    Long id,
    Long clienteId,
    String clienteNome,
    Long veiculoId,
    String veiculoPlaca,
    String status,
    LocalDateTime criadoEm,
    LocalDateTime finalizadoEm,
    List<ServicoResumoDTO> servicos,
    List<PecaResumoDTO> pecas,
    BigDecimal totalOrcamento
) {}
