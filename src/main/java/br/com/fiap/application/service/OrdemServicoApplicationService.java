package br.com.fiap.application.service;

import br.com.fiap.domain.model.*;
import br.com.fiap.domain.repository.*;
import br.com.fiap.domain.service.OrdemServicoDomainService;
import br.com.fiap.presentation.dto.request.AprovarOrcamentoRequest;
import br.com.fiap.presentation.dto.request.CriarOrdemServicoRequest;
import br.com.fiap.presentation.dto.response.OrdemServicoDTO;
import br.com.fiap.presentation.dto.response.OrdemServicoStatusDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.mapper.OrdemServicoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@Slf4j
public class OrdemServicoApplicationService {

    private static final List<OrdemServicoStatus> ORDERED_STATUSES = List.of(
        OrdemServicoStatus.RECEBIDA,
        OrdemServicoStatus.EM_DIAGNOSTICO,
        OrdemServicoStatus.AGUARDANDO_APROVACAO,
        OrdemServicoStatus.EM_EXECUCAO
    );

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;
    private final PecaRepository pecaRepository;
    private final OrdemServicoDomainService domainService;
    private final NotificacaoService notificacaoService;

    public OrdemServicoApplicationService(OrdemServicoRepository ordemServicoRepository,
                                          ClienteRepository clienteRepository,
                                          VeiculoRepository veiculoRepository,
                                          ServicoRepository servicoRepository,
                                          PecaRepository pecaRepository,
                                          OrdemServicoDomainService domainService,
                                          NotificacaoService notificacaoService) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.servicoRepository = servicoRepository;
        this.pecaRepository = pecaRepository;
        this.domainService = domainService;
        this.notificacaoService = notificacaoService;
    }

    public OrdemServicoDTO cadastraOrdemServico(CriarOrdemServicoRequest request) {
        log.info("Cadastrando ordem de servico. clienteCpfCnpj: {}, placa: {}", request.clienteCpfCnpj(), request.placaVeiculo());
        var cpfCnpj = new CpfCnpj(request.clienteCpfCnpj());
        var cliente = clienteRepository.findByCpfCnpj(cpfCnpj)
            .orElseThrow(() -> {
                log.warn("Cliente nao encontrado para CPF/CNPJ: {}", request.clienteCpfCnpj());
                return new ResourceNotFoundException("Cliente", "cpfCnpj", request.clienteCpfCnpj());
            });
        var placa = new Placa(request.placaVeiculo());
        var veiculo = veiculoRepository.findByPlaca(placa)
            .orElseThrow(() -> {
                log.warn("Veiculo nao encontrado para placa: {}", request.placaVeiculo());
                return new ResourceNotFoundException("Veiculo", "placa", request.placaVeiculo());
            });

        var servicos = request.servicosIds().stream()
                .map(id -> servicoRepository.findById(id)
                        .orElseThrow(() -> {
                            log.warn("Servico nao encontrado. id: {}", id);
                            return new ResourceNotFoundException("Servico", "id", id);
                        }))
                .toList();

        var pecasIds = request.pecasIds();
        var pecas = pecasIds != null ? pecasIds.stream()
                .map(id -> pecaRepository.findById(id)
                        .orElseThrow(() -> {
                            log.warn("Peca nao encontrada. id: {}", id);
                            return new ResourceNotFoundException("Peca", "id", id);
                        }))
                .toList() : List.<Peca>of();

        var ordemServico = new OrdemServico(cliente, veiculo, servicos, pecas);
        var salva = ordemServicoRepository.save(ordemServico);
        notificacaoService.notificarOrdemCriada(salva);
        log.info("Ordem de servico criada com sucesso. id: {}", salva.getId());
        return OrdemServicoMapper.toDTO(salva);
    }

    @Transactional(readOnly = true)
    public OrdemServicoDTO getOrdemServicoById(Long id) {
        log.info("Buscando ordem de servico por id: {}", id);
        var ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ordem de servico nao encontrada. id: {}", id);
                    return new ResourceNotFoundException("OrdemServico", "id", id);
                });
        return OrdemServicoMapper.toDTO(ordemServico);
    }

    @Transactional(readOnly = true)
    public OrdemServicoStatusDTO getStatusOrdemServico(Long id) {
        log.info("Buscando status da ordem de servico. id: {}", id);
        var ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ordem de servico nao encontrada. id: {}", id);
                    return new ResourceNotFoundException("OrdemServico", "id", id);
                });
        return OrdemServicoMapper.toStatusDTO(ordemServico);
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoDTO> getAllOrdensServicos() {
        log.info("Buscando todas as ordens de servico");
        return ordemServicoRepository.findAll().stream()
                .map(OrdemServicoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoDTO> listarOrdensComFiltros(
            List<OrdemServicoStatus> statusFilter,
            Long clienteId,
            LocalDate dataInicio,
            LocalDate dataFim,
            int page,
            int size) {
        log.info("Listando ordens com filtros. status: {}, clienteId: {}, dataInicio: {}, dataFim: {}",
                statusFilter, clienteId, dataInicio, dataFim);

        var todasOrdens = ordemServicoRepository.findAll().stream()
                .filter(os -> !os.isCompleto())
                .filter(os -> statusFilter == null || statusFilter.isEmpty() || statusFilter.contains(os.getStatus()))
                .filter(os -> clienteId == null || os.getCliente().getId().equals(clienteId))
                .filter(os -> dataInicio == null || !os.getCriadoEm().toLocalDate().isBefore(dataInicio))
                .filter(os -> dataFim == null || !os.getCriadoEm().toLocalDate().isAfter(dataFim))
                .sorted(ordemServicoComparator())
                .skip((long) page * size)
                .limit(size)
                .map(OrdemServicoMapper::toDTO)
                .toList();

        log.info("Total de {} ordens encontradas apos filtros", todasOrdens.size());
        return todasOrdens;
    }

    @Transactional(readOnly = true)
    public long countOrdensComFiltros(
            List<OrdemServicoStatus> statusFilter,
            Long clienteId,
            LocalDate dataInicio,
            LocalDate dataFim) {
        return ordemServicoRepository.findAll().stream()
                .filter(os -> !os.isCompleto())
                .filter(os -> statusFilter == null || statusFilter.isEmpty() || statusFilter.contains(os.getStatus()))
                .filter(os -> clienteId == null || os.getCliente().getId().equals(clienteId))
                .filter(os -> dataInicio == null || !os.getCriadoEm().toLocalDate().isBefore(dataInicio))
                .filter(os -> dataFim == null || !os.getCriadoEm().toLocalDate().isAfter(dataFim))
                .count();
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoDTO> getOrdensServicoByCliente(Long clienteId) {
        log.info("Buscando ordens de servico do cliente id: {}", clienteId);
        return ordemServicoRepository.findByClienteId(clienteId).stream()
                .map(OrdemServicoMapper::toDTO)
                .toList();
    }

    public OrdemServicoDTO aprovarOrcamento(Long ordemId, AprovarOrcamentoRequest request) {
        log.info("Processando aprovacao de orcamento para ordem id: {} aprovado: {}", ordemId, request.aprovado());
        var ordemServico = ordemServicoRepository.findById(ordemId)
                .orElseThrow(() -> {
                    log.warn("Ordem de servico nao encontrada. id: {}", ordemId);
                    return new ResourceNotFoundException("OrdemServico", "id", ordemId);
                });
        domainService.aprovarOrcamento(ordemServico, request.aprovado());
        var salva = ordemServicoRepository.save(ordemServico);
        notificacaoService.notificarAprovacaoOrcamento(salva, request.aprovado(), request.observacoes());
        log.info("Orcamento da ordem {} foi {} com sucesso", ordemId, request.aprovado() ? "APROVADO" : "RECUSADO");
        return OrdemServicoMapper.toDTO(salva);
    }

    public void avancaStatusDaOrdemDeServico(Long orderId) {
        log.info("Avancando status da ordem de servico. id: {}", orderId);
        var ordemServico = ordemServicoRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Ordem de servico nao encontrada. id: {}", orderId);
                    return new ResourceNotFoundException("OrdemServico", "id", orderId);
                });
        var statusAnterior = ordemServico.getStatus().name();
        domainService.avancaStatus(ordemServico);
        ordemServicoRepository.save(ordemServico);
        notificacaoService.notificarAtualizacaoStatus(ordemServico, statusAnterior, ordemServico.getStatus().name());
        log.info("Status da ordem atualizado com sucesso. id: {}", orderId);
    }

    public void deletaOrdemServico(Long id) {
        if (ordemServicoRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("OrdemServico", "id", id);
        }
        ordemServicoRepository.deleteById(id);
    }

    private Comparator<OrdemServico> ordemServicoComparator() {
        Comparator<OrdemServico> byPriority = Comparator.comparingInt(
            os -> ORDERED_STATUSES.contains(os.getStatus())
                ? ORDERED_STATUSES.indexOf(os.getStatus())
                : ORDERED_STATUSES.size()
        );
        Comparator<OrdemServico> byDate = Comparator.comparing(OrdemServico::getCriadoEm);
        return byPriority.thenComparing(byDate);
    }
}
