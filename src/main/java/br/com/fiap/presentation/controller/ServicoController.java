package br.com.fiap.presentation.controller;

import br.com.fiap.application.service.ServicoService;
import br.com.fiap.presentation.dto.request.AtualizarServicoRequest;
import br.com.fiap.presentation.dto.request.CriarServicoRequest;
import br.com.fiap.presentation.dto.response.ServicoDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.mapper.ServicoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/servicos")
@Slf4j
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoDTO> cadastraServico(@Valid @RequestBody CriarServicoRequest request) {
        log.info("Cadastrando servico: {}", request.nome());
        var response = servicoService.cadastraServico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> getServico(@PathVariable Long id) {
        log.info("Buscando servico por id: {}", id);
        var servico = servicoService.getServicoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servico", "id", id));
        return ResponseEntity.ok(ServicoMapper.toDTO(servico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizaServico(@PathVariable Long id,
                                                       @Valid @RequestBody AtualizarServicoRequest request) {
        log.info("Atualizando servico id: {}", id);
        var response = servicoService.atualizaServico(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaServico(@PathVariable Long id) {
        log.info("Deletando servico id: {}", id);
        servicoService.deletaServico(id);
        return ResponseEntity.noContent().build();
    }
}
