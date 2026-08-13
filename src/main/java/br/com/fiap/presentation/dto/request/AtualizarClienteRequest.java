package br.com.fiap.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AtualizarClienteRequest(
    @NotBlank(message = "Nome obrigatorio")
    String nome,

    @NotBlank(message = "Email obrigatorio")
    String email,

    @NotBlank(message = "Telefone obrigatorio")
    String telefone
) {}
