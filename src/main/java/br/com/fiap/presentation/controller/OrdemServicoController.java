package br.com.fiap.presentation.controller;

import br.com.fiap.application.service.OrdemServicoApplicationService;
import br.com.fiap.domain.model.OrdemServicoStatus;
import br.com.fiap.presentation.dto.request.AprovarOrcamentoRequest;
import br.com.fiap.presentation.dto.request.CriarOrdemServicoRequest;
import br.com.fiap.presentation.dto.response.OrdemServicoDTO;
import br.com.fiap.presentation.dto.response.OrdemServicoStatusDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class OrdemServicoController {

    private final OrdemServicoApplicationService ordemServicoApplicationService;

    public OrdemServicoController(OrdemServicoApplicationService ordemServicoApplicationService) {
        this.ordemServicoApplicationService = ordemServicoApplicationService;
    }

    @PostMapping("/admin/ordem")
    public ResponseEntity<OrdemServicoDTO> cadastraOrdemServico(@Valid @RequestBody CriarOrdemServicoRequest request) {
        log.info("Cadastrando ordem de servico. clienteCpfCnpj: {}, placa: {}", request.clienteCpfCnpj(), request.placaVeiculo());
        var response = ordemServicoApplicationService.cadastraOrdemServico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/admin/ordem")
    public ResponseEntity<Map<String, Object>> getAllOrdensServicos(
            @RequestParam(required = false) List<OrdemServicoStatus> status,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Listando ordens de servico com filtros.");
        var ordens = ordemServicoApplicationService.listarOrdensComFiltros(status, clienteId, dataInicio, dataFim, page, size);
        var total = ordemServicoApplicationService.countOrdensComFiltros(status, clienteId, dataInicio, dataFim);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", ordens);
        response.put("totalElements", total);
        response.put("totalPages", (int) Math.ceil((double) total / size));
        response.put("currentPage", page);
        response.put("size", size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/ordem/{id}")
    public ResponseEntity<OrdemServicoDTO> getOrdemServico(@PathVariable Long id) {
        log.info("Buscando ordem de servico por id: {}", id);
        var response = ordemServicoApplicationService.getOrdemServicoById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/ordem/{id}/status")
    public ResponseEntity<OrdemServicoStatusDTO> getStatusOrdemServico(@PathVariable Long id) {
        log.info("Buscando status da ordem de servico. id: {}", id);
        var response = ordemServicoApplicationService.getStatusOrdemServico(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/ordem/{id}/aprovar-orcamento")
    public ResponseEntity<OrdemServicoDTO> aprovarOrcamento(
            @PathVariable Long id,
            @Valid @RequestBody AprovarOrcamentoRequest request) {
        log.info("Aprovando orcamento da ordem id: {}", id);
        var response = ordemServicoApplicationService.aprovarOrcamento(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/ordem/{id}/avanca")
    public ResponseEntity<Void> avancaStatusDaOrdemDeServico(@PathVariable Long id) {
        log.info("Avancando status da ordem de servico. id: {}", id);
        ordemServicoApplicationService.avancaStatusDaOrdemDeServico(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/ordem/{id}")
    public ResponseEntity<Void> deletaOrdemServico(@PathVariable Long id) {
        log.info("Deletando ordem de servico. id: {}", id);
        ordemServicoApplicationService.deletaOrdemServico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publico/ordem/cliente/{clienteId}")
    public ResponseEntity<List<OrdemServicoDTO>> getOrdemByCliente(@PathVariable Long clienteId) {
        log.info("Buscando ordens de servico do cliente id: {}", clienteId);
        var ordens = ordemServicoApplicationService.getOrdensServicoByCliente(clienteId);
        return ResponseEntity.ok(ordens);
    }

    @PostMapping("/publico/ordem/{id}/aprovar-orcamento")
    public ResponseEntity<OrdemServicoDTO> aprovarOrcamentoPublico(
            @PathVariable Long id,
            @Valid @RequestBody AprovarOrcamentoRequest request) {
        log.info("Cliente aprovando/recusando orcamento da ordem id: {}", id);
        var response = ordemServicoApplicationService.aprovarOrcamento(id, request);
        return ResponseEntity.ok(response);
    }

}
