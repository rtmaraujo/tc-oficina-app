package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.OrdemServico;
import br.com.fiap.domain.repository.OrdemServicoRepository;
import br.com.fiap.infrastructure.persistence.entity.*;
import br.com.fiap.infrastructure.persistence.mapper.OrdemServicoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrdemServicoRepositoryImpl implements OrdemServicoRepository {

    private final OrdemServicoJpaRepository jpaRepository;
    private final ClienteJpaRepository clienteJpaRepository;
    private final VeiculoJpaRepository veiculoJpaRepository;
    private final ServicoJpaRepository servicoJpaRepository;
    private final PecaJpaRepository pecaJpaRepository;

    public OrdemServicoRepositoryImpl(OrdemServicoJpaRepository jpaRepository,
                                      ClienteJpaRepository clienteJpaRepository,
                                      VeiculoJpaRepository veiculoJpaRepository,
                                      ServicoJpaRepository servicoJpaRepository,
                                      PecaJpaRepository pecaJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.clienteJpaRepository = clienteJpaRepository;
        this.veiculoJpaRepository = veiculoJpaRepository;
        this.servicoJpaRepository = servicoJpaRepository;
        this.pecaJpaRepository = pecaJpaRepository;
    }

    @Override
    public OrdemServico save(OrdemServico ordemServico) {
        ClienteEntity managedCliente = clienteJpaRepository.getReferenceById(ordemServico.getCliente().getId());
        VeiculoEntity managedVeiculo = veiculoJpaRepository.getReferenceById(ordemServico.getVeiculo().getId());

        List<ServicoEntity> managedServicos = ordemServico.getServicos().stream()
                .map(s -> servicoJpaRepository.getReferenceById(s.getId()))
                .toList();
        List<PecaEntity> managedPecas = ordemServico.getPecas().stream()
                .map(p -> pecaJpaRepository.getReferenceById(p.getId()))
                .toList();

        var entity = new OrdemServicoEntity(
            ordemServico.getId(),
            managedCliente,
            managedVeiculo,
            ordemServico.getStatus(),
            ordemServico.getCriadoEm(),
            ordemServico.getFinalizadoEm(),
            managedServicos,
            managedPecas,
            ordemServico.getTotalOrcamento()
        );
        var saved = jpaRepository.save(entity);
        return OrdemServicoMapper.toDomain(saved);
    }

    @Override
    public Optional<OrdemServico> findById(Long id) {
        return jpaRepository.findById(id).map(OrdemServicoMapper::toDomain);
    }

    @Override
    public List<OrdemServico> findAll() {
        return jpaRepository.findAll().stream()
                .map(OrdemServicoMapper::toDomain)
                .toList();
    }

    @Override
    public List<OrdemServico> findByClienteId(Long clienteId) {
        return jpaRepository.findByClienteId(clienteId).stream()
                .map(OrdemServicoMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
