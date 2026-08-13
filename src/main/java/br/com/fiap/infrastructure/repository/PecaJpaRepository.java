package br.com.fiap.infrastructure.repository;

import br.com.fiap.infrastructure.persistence.entity.PecaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PecaJpaRepository extends JpaRepository<PecaEntity, Long> {
}
