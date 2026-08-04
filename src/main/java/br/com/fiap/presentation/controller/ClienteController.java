package br.com.fiap.presentation.controller;

import br.com.fiap.application.service.ClienteService;
import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import br.com.fiap.presentation.dto.request.AtualizarClienteRequest;
import br.com.fiap.presentation.dto.request.CriarClienteRequest;
import br.com.fiap.presentation.dto.response.ClienteDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.exception.ValidationException;
import br.com.fiap.presentation.mapper.ClienteMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/clientes")
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> cadastraCliente(@Valid @RequestBody CriarClienteRequest request) {
        log.info("Cadastrando cliente: {}", request.nome());
        var response = clienteService.cadastraCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getCliente(@PathVariable Long id) {
        log.info("Buscando cliente por id: {}", id);
        var cliente = clienteService.getClienteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));
        return ResponseEntity.ok(ClienteMapper.toDTO(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizaCliente(@PathVariable Long id,
                                                       @Valid @RequestBody AtualizarClienteRequest request) {
        log.info("Atualizando cliente id: {}", id);
        var response = clienteService.atualizaCliente(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaCliente(@PathVariable Long id) {
        log.info("Deletando cliente id: {}", id);
        clienteService.deletaCliente(id);
        return ResponseEntity.noContent().build();
    }
}
