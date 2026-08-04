package br.com.fiap.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CriarClienteRequest(
    @NotBlank(message = "Nome obrigatorio")
    String nome,

    @NotBlank(message = "CPF/CNPJ obrigatorio")
    String cpfCnpj,

    @NotBlank(message = "Email obrigatorio")
    @Email(message = "Email invalido")
    String email,

    @NotBlank(message = "Telefone obrigatorio")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 digitos")
    String telefone
) {}
