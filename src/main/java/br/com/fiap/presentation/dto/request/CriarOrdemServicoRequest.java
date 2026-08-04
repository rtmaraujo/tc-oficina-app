package br.com.fiap.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CriarOrdemServicoRequest(
    @NotBlank(message = "CPF/CNPJ do cliente obrigatorio")
    String clienteCpfCnpj,

    @NotBlank(message = "Placa do veiculo obrigatoria")
    String placaVeiculo,

    @NotEmpty(message = "Pelo menos um servico deve ser informado")
    List<Long> servicosIds,

    List<Long> pecasIds
) {}
