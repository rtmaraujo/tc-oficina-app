package br.com.fiap.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdemServicoStatusDTO(
    Long id,
    String status,
    BigDecimal orcamento,
    LocalDateTime ultimaAtualizacao
) {}
