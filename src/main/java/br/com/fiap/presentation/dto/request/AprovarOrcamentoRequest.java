package br.com.fiap.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record AprovarOrcamentoRequest(
    @NotNull(message = "Campo aprovado obrigatorio")
    Boolean aprovado,

    String observacoes
) {}
