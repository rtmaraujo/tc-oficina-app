package br.com.fiap.application.service;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import br.com.fiap.domain.repository.ClienteRepository;
import br.com.fiap.presentation.dto.request.AtualizarClienteRequest;
import br.com.fiap.presentation.dto.request.CriarClienteRequest;
import br.com.fiap.presentation.dto.response.ClienteDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.exception.ValidationException;
import br.com.fiap.presentation.mapper.ClienteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO cadastraCliente(CriarClienteRequest request) {
        log.info("Iniciando cadastro de cliente: {}", request.nome());
        var cpfCnpj = new CpfCnpj(request.cpfCnpj());
        if (clienteRepository.findByCpfCnpj(cpfCnpj).isPresent()) {
            log.warn("CPF/CNPJ ja existente: {}", request.cpfCnpj());
            throw new ValidationException("cpfCnpj", "Ja existe um cliente com este CPF/CNPJ.", "CPF_CNPJ_DUPLICADO");
        }
        var cliente = new Cliente(request.nome(), cpfCnpj, request.email(), request.telefone());
        var salvo = clienteRepository.save(cliente);
        log.info("Cliente cadastrado com sucesso. id: {}", salvo.getId());
        return ClienteMapper.toDTO(salvo);
    }

    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> getClientByCpfCnpj(CpfCnpj cpfCnpj) {
        return clienteRepository.findByCpfCnpj(cpfCnpj);
    }

    public ClienteDTO atualizaCliente(Long id, AtualizarClienteRequest request) {
        log.info("Atualizando cliente id: {}", id);
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente nao encontrado para atualizacao. id: {}", id);
                    return new ResourceNotFoundException("Cliente", "id", id);
                });
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
        var atualizado = clienteRepository.save(cliente);
        log.info("Cliente atualizado com sucesso. id: {}", id);
        return ClienteMapper.toDTO(atualizado);
    }

    public void deletaCliente(Long id) {
        log.info("Deletando cliente id: {}", id);
        if (clienteRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Cliente", "id", id);
        }
        clienteRepository.deleteById(id);
        log.info("Cliente deletado com sucesso. id: {}", id);
    }
}
