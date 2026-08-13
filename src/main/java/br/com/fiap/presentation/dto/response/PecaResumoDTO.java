package br.com.fiap.presentation.dto.response;

import java.math.BigDecimal;

public record PecaResumoDTO(
    Long id,
    String nome,
    BigDecimal preco,
    Integer qtdEstoque
) {}
