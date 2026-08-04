package br.com.fiap.presentation.dto.request;

import java.math.BigDecimal;

public record AtualizarServicoRequest(
    String nome,
    String descricao,
    BigDecimal preco
) {}
