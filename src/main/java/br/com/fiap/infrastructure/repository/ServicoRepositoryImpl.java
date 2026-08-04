package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.Servico;
import br.com.fiap.domain.repository.ServicoRepository;
import br.com.fiap.infrastructure.persistence.mapper.ServicoMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ServicoRepositoryImpl implements ServicoRepository {

    private final ServicoJpaRepository jpaRepository;

    public ServicoRepositoryImpl(ServicoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Servico save(Servico servico) {
        var entity = ServicoMapper.toEntity(servico);
        var saved = jpaRepository.save(entity);
        return ServicoMapper.toDomain(saved);
    }

    @Override
    public Optional<Servico> findById(Long id) {
        return jpaRepository.findById(id).map(ServicoMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
