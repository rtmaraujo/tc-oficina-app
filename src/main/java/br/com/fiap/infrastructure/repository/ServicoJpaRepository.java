package br.com.fiap.infrastructure.repository;

import br.com.fiap.infrastructure.persistence.entity.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoJpaRepository extends JpaRepository<ServicoEntity, Long> {
}
