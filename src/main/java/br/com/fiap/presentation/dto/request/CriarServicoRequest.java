package br.com.fiap.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CriarServicoRequest(
    @NotBlank(message = "Nome obrigatorio")
    String nome,

    String descricao,

    @Positive(message = "Preco deve ser maior que zero")
    BigDecimal preco
) {}
