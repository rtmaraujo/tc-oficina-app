package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.Peca;
import br.com.fiap.infrastructure.persistence.entity.PecaEntity;

public class PecaMapper {

    public static PecaEntity toEntity(Peca domain) {
        if (domain == null) return null;
        return new PecaEntity(
            domain.getId(),
            domain.getNome(),
            domain.getDescricao(),
            domain.getPreco(),
            domain.getQtdEstoque()
        );
    }

    public static Peca toDomain(PecaEntity entity) {
        if (entity == null) return null;
        return new Peca(
            entity.getId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getPreco(),
            entity.getQtdEstoque()
        );
    }
}
