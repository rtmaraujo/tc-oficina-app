package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import br.com.fiap.domain.repository.ClienteRepository;
import br.com.fiap.infrastructure.persistence.entity.CpfCnpjEntity;
import br.com.fiap.infrastructure.persistence.mapper.ClienteMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;

    public ClienteRepositoryImpl(ClienteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        var entity = ClienteMapper.toEntity(cliente);
        var saved = jpaRepository.save(entity);
        return ClienteMapper.toDomain(saved);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaRepository.findById(id).map(ClienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> findByCpfCnpj(CpfCnpj cpfCnpj) {
        var entity = new CpfCnpjEntity(cpfCnpj.getValue());
        return jpaRepository.findByCpfCnpj(entity).map(ClienteMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
