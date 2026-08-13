package br.com.fiap.presentation.dto.request;

import java.math.BigDecimal;

public record AtualizarPecaRequest(
    String nome,
    String descricao,
    BigDecimal preco,
    Integer qtdEstoque
) {}
