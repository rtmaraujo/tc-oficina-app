package br.com.fiap.presentation.dto.request;

public record AtualizarVeiculoRequest(
    String marca,
    String modelo,
    Integer ano
) {}
