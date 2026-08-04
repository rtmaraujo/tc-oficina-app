package br.com.fiap.domain.repository;

import br.com.fiap.domain.model.Veiculo;
import br.com.fiap.domain.model.Placa;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository {
    Veiculo save(Veiculo veiculo);
    Optional<Veiculo> findById(Long id);
    Optional<Veiculo> findByPlaca(Placa licensePlate);
    List<Veiculo> findByClienteId(Long clienteId);
    void deleteById(Long id);
}
