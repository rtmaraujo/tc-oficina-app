package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.Peca;
import br.com.fiap.domain.repository.PecaRepository;
import br.com.fiap.infrastructure.persistence.mapper.PecaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PecaRepositoryImpl implements PecaRepository {

    private final PecaJpaRepository jpaRepository;

    public PecaRepositoryImpl(PecaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Peca save(Peca peca) {
        var entity = PecaMapper.toEntity(peca);
        var saved = jpaRepository.save(entity);
        return PecaMapper.toDomain(saved);
    }

    @Override
    public Optional<Peca> findById(Long id) {
        return jpaRepository.findById(id).map(PecaMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
