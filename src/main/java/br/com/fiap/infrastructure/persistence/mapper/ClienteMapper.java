package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import br.com.fiap.infrastructure.persistence.entity.ClienteEntity;
import br.com.fiap.infrastructure.persistence.entity.CpfCnpjEntity;

public class ClienteMapper {

    public static ClienteEntity toEntity(Cliente domain) {
        if (domain == null) return null;
        return new ClienteEntity(
            domain.getId(),
            domain.getNome(),
            domain.getCpfCnpj() != null ? new CpfCnpjEntity(domain.getCpfCnpj().getValue()) : null,
            domain.getEmail(),
            domain.getTelefone()
        );
    }

    public static Cliente toDomain(ClienteEntity entity) {
        if (entity == null) return null;
        var cpfCnpj = entity.getCpfCnpj() != null
            ? new CpfCnpj(entity.getCpfCnpj().getValue())
            : null;
        return new Cliente(
            entity.getId(),
            entity.getNome(),
            cpfCnpj,
            entity.getEmail(),
            entity.getTelefone()
        );
    }
}
