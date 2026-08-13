package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.Placa;
import br.com.fiap.domain.model.Veiculo;
import br.com.fiap.domain.repository.VeiculoRepository;
import br.com.fiap.infrastructure.persistence.entity.ClienteEntity;
import br.com.fiap.infrastructure.persistence.entity.PlacaEntity;
import br.com.fiap.infrastructure.persistence.entity.VeiculoEntity;
import br.com.fiap.infrastructure.persistence.mapper.VeiculoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VeiculoRepositoryImpl implements VeiculoRepository {

    private final VeiculoJpaRepository jpaRepository;
    private final ClienteJpaRepository clienteJpaRepository;

    public VeiculoRepositoryImpl(VeiculoJpaRepository jpaRepository, ClienteJpaRepository clienteJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.clienteJpaRepository = clienteJpaRepository;
    }

    @Override
    public Veiculo save(Veiculo veiculo) {
        ClienteEntity managedCliente = clienteJpaRepository.getReferenceById(veiculo.getCliente().getId());
        var placaEntity = new PlacaEntity(veiculo.getPlaca().getValue());
        var entity = new VeiculoEntity(
            veiculo.getId(), placaEntity, veiculo.getMarca(),
            veiculo.getModelo(), veiculo.getAno(), managedCliente);
        var saved = jpaRepository.save(entity);
        return VeiculoMapper.toDomain(saved);
    }

    @Override
    public Optional<Veiculo> findById(Long id) {
        return jpaRepository.findById(id).map(VeiculoMapper::toDomain);
    }

    @Override
    public Optional<Veiculo> findByPlaca(Placa placa) {
        var placaEntity = new PlacaEntity(placa.getValue());
        return jpaRepository.findByPlaca(placaEntity).map(VeiculoMapper::toDomain);
    }

    @Override
    public List<Veiculo> findByClienteId(Long clienteId) {
        return jpaRepository.findByClienteId(clienteId).stream()
                .map(VeiculoMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
