package br.com.fiap.presentation.controller;

import br.com.fiap.application.service.VeiculoService;
import br.com.fiap.presentation.dto.request.AtualizarVeiculoRequest;
import br.com.fiap.presentation.dto.request.CriarVeiculoRequest;
import br.com.fiap.presentation.dto.response.VeiculoDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.mapper.VeiculoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/veiculos")
@Slf4j
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    public ResponseEntity<VeiculoDTO> cadastraVeiculo(@Valid @RequestBody CriarVeiculoRequest request) {
        log.info("Cadastrando veiculo. placa: {}", request.placa());
        var response = veiculoService.cadastraVeiculo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> getVeiculo(@PathVariable Long id) {
        log.info("Buscando veiculo por id: {}", id);
        var veiculo = veiculoService.getVeiculoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veiculo", "id", id));
        return ResponseEntity.ok(VeiculoMapper.toDTO(veiculo));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VeiculoDTO>> getVeiculoByCliente(@PathVariable Long clienteId) {
        log.info("Buscando veiculos do cliente id: {}", clienteId);
        var veiculos = veiculoService.getVeiculosByClienteId(clienteId);
        return ResponseEntity.ok(veiculos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> atualizaVeiculo(@PathVariable Long id,
                                                       @Valid @RequestBody AtualizarVeiculoRequest request) {
        log.info("Atualizando veiculo id: {}", id);
        var response = veiculoService.atualizaVeiculo(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaVeiculo(@PathVariable Long id) {
        log.info("Deletando veiculo id: {}", id);
        veiculoService.deletaVeiculo(id);
        return ResponseEntity.noContent().build();
    }
}
