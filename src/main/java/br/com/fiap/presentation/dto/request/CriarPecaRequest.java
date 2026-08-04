package br.com.fiap.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record CriarPecaRequest(
    @NotBlank(message = "Nome obrigatorio")
    String nome,

    String descricao,

    @Positive(message = "Preco deve ser maior que zero")
    BigDecimal preco,

    @PositiveOrZero(message = "Quantidade em estoque nao pode ser negativa")
    Integer qtdEstoque
) {}
