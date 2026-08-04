package br.com.fiap.presentation.dto.response;

import java.math.BigDecimal;

public record ServicoResumoDTO(
    Long id,
    String nome,
    BigDecimal preco
) {}
