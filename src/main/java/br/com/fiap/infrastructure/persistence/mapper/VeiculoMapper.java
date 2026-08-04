package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.Placa;
import br.com.fiap.domain.model.Veiculo;
import br.com.fiap.infrastructure.persistence.entity.PlacaEntity;
import br.com.fiap.infrastructure.persistence.entity.VeiculoEntity;

public class VeiculoMapper {

    public static VeiculoEntity toEntity(Veiculo domain) {
        if (domain == null) return null;
        return new VeiculoEntity(
            domain.getId(),
            domain.getPlaca() != null ? new PlacaEntity(domain.getPlaca().getValue()) : null,
            domain.getMarca(),
            domain.getModelo(),
            domain.getAno(),
            domain.getCliente() != null
                ? ClienteMapper.toEntity(domain.getCliente())
                : null
        );
    }

    public static Veiculo toDomain(VeiculoEntity entity) {
        if (entity == null) return null;
        var placa = entity.getPlaca() != null
            ? new Placa(entity.getPlaca().getValue())
            : null;
        var cliente = entity.getCliente() != null
            ? ClienteMapper.toDomain(entity.getCliente())
            : null;
        return new Veiculo(
            entity.getId(),
            placa,
            entity.getMarca(),
            entity.getModelo(),
            entity.getAno(),
            cliente
        );
    }
}
