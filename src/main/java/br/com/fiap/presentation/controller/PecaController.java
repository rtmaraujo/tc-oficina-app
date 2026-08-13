package br.com.fiap.presentation.controller;

import br.com.fiap.application.service.PecaService;
import br.com.fiap.presentation.dto.request.AtualizarPecaRequest;
import br.com.fiap.presentation.dto.request.CriarPecaRequest;
import br.com.fiap.presentation.dto.response.PecaDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.mapper.PecaMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/pecas")
@Slf4j
public class PecaController {

    private final PecaService pecaService;

    public PecaController(PecaService pecaService) {
        this.pecaService = pecaService;
    }

    @PostMapping
    public ResponseEntity<PecaDTO> cadastraPeca(@Valid @RequestBody CriarPecaRequest request) {
        log.info("Cadastrando peca: {}", request.nome());
        var response = pecaService.cadastraPeca(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PecaDTO> getPeca(@PathVariable Long id) {
        log.info("Buscando peca por id: {}", id);
        var peca = pecaService.getPecaById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Peca", "id", id));
        return ResponseEntity.ok(PecaMapper.toDTO(peca));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PecaDTO> atualizaPeca(@PathVariable Long id,
                                                 @Valid @RequestBody AtualizarPecaRequest request) {
        log.info("Atualizando peca id: {}", id);
        var response = pecaService.atualizaPeca(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaPeca(@PathVariable Long id) {
        log.info("Deletando peca id: {}", id);
        pecaService.deletaPeca(id);
        return ResponseEntity.noContent().build();
    }
}
