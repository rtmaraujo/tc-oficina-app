package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.Servico;
import br.com.fiap.infrastructure.persistence.entity.ServicoEntity;

public class ServicoMapper {

    public static ServicoEntity toEntity(Servico domain) {
        if (domain == null) return null;
        return new ServicoEntity(
            domain.getId(),
            domain.getNome(),
            domain.getDescricao(),
            domain.getPreco()
        );
    }

    public static Servico toDomain(ServicoEntity entity) {
        if (entity == null) return null;
        return new Servico(
            entity.getId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getPreco()
        );
    }
}
