package br.com.fiap.infrastructure.repository;

import br.com.fiap.infrastructure.persistence.entity.OrdemServicoEntity;
import br.com.fiap.domain.model.OrdemServicoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoJpaRepository extends JpaRepository<OrdemServicoEntity, Long> {
    List<OrdemServicoEntity> findByClienteId(Long clienteId);
    List<OrdemServicoEntity> findByStatus(OrdemServicoStatus status);
}
