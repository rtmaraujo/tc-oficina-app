package br.com.fiap.infrastructure.repository;

import br.com.fiap.infrastructure.persistence.entity.VeiculoEntity;
import br.com.fiap.infrastructure.persistence.entity.PlacaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeiculoJpaRepository extends JpaRepository<VeiculoEntity, Long> {
    Optional<VeiculoEntity> findByPlaca(PlacaEntity placa);
    List<VeiculoEntity> findByClienteId(Long clienteId);
}
