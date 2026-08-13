package br.com.fiap.domain.repository;

import br.com.fiap.domain.model.Peca;

import java.util.Optional;

public interface PecaRepository {
    Peca save(Peca peca);
    Optional<Peca> findById(Long id);
    void deleteById(Long id);
}
