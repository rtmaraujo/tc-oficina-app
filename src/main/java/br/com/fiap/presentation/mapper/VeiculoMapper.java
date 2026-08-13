package br.com.fiap.presentation.mapper;

import br.com.fiap.domain.model.Veiculo;
import br.com.fiap.presentation.dto.response.VeiculoDTO;
import br.com.fiap.presentation.dto.response.VeiculoResumoDTO;

public class VeiculoMapper {

    public static VeiculoDTO toDTO(Veiculo veiculo) {
        if (veiculo == null) return null;
        return new VeiculoDTO(
            veiculo.getId(),
            veiculo.getPlaca() != null ? veiculo.getPlaca().getValue() : null,
            veiculo.getMarca(),
            veiculo.getModelo(),
            veiculo.getAno(),
            veiculo.getCliente() != null ? veiculo.getCliente().getId() : null
        );
    }

    public static VeiculoResumoDTO toResumoDTO(Veiculo veiculo) {
        if (veiculo == null) return null;
        return new VeiculoResumoDTO(
            veiculo.getId(),
            veiculo.getPlaca() != null ? veiculo.getPlaca().getValue() : null,
            veiculo.getMarca(),
            veiculo.getModelo(),
            veiculo.getAno()
        );
    }
}
