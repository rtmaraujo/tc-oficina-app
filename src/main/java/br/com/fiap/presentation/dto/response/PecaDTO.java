package br.com.fiap.presentation.dto.response;

import java.math.BigDecimal;

public record PecaDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer qtdEstoque
) {}
