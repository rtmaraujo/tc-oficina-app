package br.com.fiap.presentation.dto.response;

import java.math.BigDecimal;

public record ServicoDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    String tipoServico
) {}
