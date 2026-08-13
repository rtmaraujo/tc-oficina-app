package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.*;
import br.com.fiap.infrastructure.persistence.entity.*;

import java.util.stream.Collectors;

public class OrdemServicoMapper {

    public static OrdemServicoEntity toEntity(OrdemServico domain) {
        if (domain == null) return null;
        return new OrdemServicoEntity(
            domain.getId(),
            domain.getCliente() != null ? ClienteMapper.toEntity(domain.getCliente()) : null,
            domain.getVeiculo() != null ? VeiculoMapper.toEntity(domain.getVeiculo()) : null,
            domain.getStatus(),
            domain.getCriadoEm(),
            domain.getFinalizadoEm(),
            domain.getServicos() != null
                ? domain.getServicos().stream().map(ServicoMapper::toEntity).collect(Collectors.toList())
                : null,
            domain.getPecas() != null
                ? domain.getPecas().stream().map(PecaMapper::toEntity).collect(Collectors.toList())
                : null,
            domain.getTotalOrcamento()
        );
    }

    public static OrdemServico toDomain(OrdemServicoEntity entity) {
        if (entity == null) return null;
        var cliente = entity.getCliente() != null
            ? ClienteMapper.toDomain(entity.getCliente())
            : null;
        var veiculo = entity.getVeiculo() != null
            ? VeiculoMapper.toDomain(entity.getVeiculo())
            : null;
        var servicos = entity.getServicos() != null
            ? entity.getServicos().stream().map(ServicoMapper::toDomain).collect(Collectors.toList())
            : null;
        var pecas = entity.getPecas() != null
            ? entity.getPecas().stream().map(PecaMapper::toDomain).collect(Collectors.toList())
            : null;

        var ordem = new OrdemServico(
            entity.getId(),
            cliente,
            veiculo,
            servicos,
            pecas,
            entity.getCriadoEm()
        );
        ordem.atualizaStatus(entity.getStatus());
        if (entity.getFinalizadoEm() != null) {
            ordem.setFinalizadoEm(entity.getFinalizadoEm());
        }
        if (entity.getTotalOrcamento() != null) {
            ordem.setTotalOrcamento(entity.getTotalOrcamento());
        }
        return ordem;
    }
}
