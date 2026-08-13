package br.com.fiap.presentation.dto.response;

public record VeiculoDTO(
    Long id,
    String placa,
    String marca,
    String modelo,
    Integer ano,
    Long clienteId
) {}
