package br.com.fiap.domain.repository;

import br.com.fiap.domain.model.OrdemServico;

import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository {
    OrdemServico save(OrdemServico ordemServico);
    Optional<OrdemServico> findById(Long id);
    List<OrdemServico> findAll();
    List<OrdemServico> findByClienteId(Long clienteId);
    void deleteById(Long id);
}
