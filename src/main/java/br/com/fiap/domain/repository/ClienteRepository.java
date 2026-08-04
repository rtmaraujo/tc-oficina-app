package br.com.fiap.domain.repository;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import java.util.Optional;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(Long id);
    Optional<Cliente> findByCpfCnpj(CpfCnpj cpfCnpj);
    void deleteById(Long id);
}
