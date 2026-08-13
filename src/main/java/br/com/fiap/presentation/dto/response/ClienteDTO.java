package br.com.fiap.presentation.dto.response;

public record ClienteDTO(
    Long id,
    String nome,
    String cpfCnpj,
    String email,
    String telefone
) {}
