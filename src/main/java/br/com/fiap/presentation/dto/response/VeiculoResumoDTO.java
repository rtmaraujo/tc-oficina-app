package br.com.fiap.presentation.dto.response;

public record VeiculoResumoDTO(
    Long id,
    String placa,
    String marca,
    String modelo,
    Integer ano
) {}
