package br.com.fiap.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarVeiculoRequest(
    @NotBlank(message = "Placa obrigatoria")
    String placa,

    @NotBlank(message = "Marca obrigatoria")
    String marca,

    @NotBlank(message = "Modelo obrigatorio")
    String modelo,

    @NotNull(message = "Ano obrigatorio")
    Integer ano,

    @NotNull(message = "Cliente ID obrigatorio")
    Long clienteId
) {}
