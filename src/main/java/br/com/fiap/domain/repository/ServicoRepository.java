package br.com.fiap.domain.repository;

import br.com.fiap.domain.model.Servico;
import java.util.Optional;

public interface ServicoRepository {
    Servico save(Servico service);
    Optional<Servico> findById(Long id);
    void deleteById(Long id);
}
