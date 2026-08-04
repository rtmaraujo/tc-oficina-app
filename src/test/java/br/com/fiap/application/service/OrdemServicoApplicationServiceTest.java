package br.com.fiap.application.service;

import br.com.fiap.domain.model.*;
import br.com.fiap.domain.repository.*;
import br.com.fiap.domain.service.OrdemServicoDomainService;
import br.com.fiap.presentation.dto.request.AprovarOrcamentoRequest;
import br.com.fiap.presentation.dto.request.CriarOrdemServicoRequest;
import br.com.fiap.presentation.dto.response.OrdemServicoDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrdemServicoApplicationService - Testes de Aplicacao")
class OrdemServicoApplicationServiceTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private PecaRepository pecaRepository;

    @Mock
    private OrdemServicoDomainService domainService;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private OrdemServicoApplicationService ordemServicoApplicationService;

    private Cliente cliente;
    private Veiculo veiculo;
    private Servico servico;
    private Peca peca;

    @BeforeEach
    void setUp() {
        var cpfCnpj = new CpfCnpj("12345678909");
        cliente = new Cliente("John Doe", cpfCnpj, "john@example.com", "123456789");

        var placa = new Placa("ABC1234");
        veiculo = new Veiculo(placa, "Toyota", "Corolla", 2020, cliente);

        servico = new Servico("Troca de Oleo", "Troca completa", new BigDecimal("150.00"));
        peca = new Peca("Oleo Sintetico 5L", "Oleo", new BigDecimal("85.50"), 10);
    }

    @Nested
    @DisplayName("Cadastrar Ordem de Servico")
    class CadastrarOrdemServicoTests {

        @Test
        @DisplayName("Deve criar ordem de servico com dados validos")
        void shouldCreateOrderServiceWithValidData() {
            var request = new CriarOrdemServicoRequest("12345678909", "ABC1234", Arrays.asList(1L), Arrays.asList(1L));
            var ordemServico = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));

            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.of(cliente));
            when(veiculoRepository.findByPlaca(any())).thenReturn(Optional.of(veiculo));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));
            when(ordemServicoRepository.save(any(OrdemServico.class))).thenReturn(ordemServico);

            OrdemServicoDTO result = ordemServicoApplicationService.cadastraOrdemServico(request);

            assertNotNull(result);
            verify(clienteRepository).findByCpfCnpj(any());
            verify(veiculoRepository).findByPlaca(any());
            verify(servicoRepository).findById(1L);
            verify(pecaRepository).findById(1L);
            verify(ordemServicoRepository).save(any(OrdemServico.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando cliente nao encontrado")
        void shouldThrowExceptionWhenClientNotFound() {
            var request = new CriarOrdemServicoRequest("12345678909", "ABC1234", Arrays.asList(1L), Arrays.asList(1L));
            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                ordemServicoApplicationService.cadastraOrdemServico(request)
            );
            verify(ordemServicoRepository, never()).save(any(OrdemServico.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando veiculo nao encontrado")
        void shouldThrowExceptionWhenVehicleNotFound() {
            var request = new CriarOrdemServicoRequest("12345678909", "ABC1234", Arrays.asList(1L), Arrays.asList(1L));
            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.of(cliente));
            when(veiculoRepository.findByPlaca(any())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                ordemServicoApplicationService.cadastraOrdemServico(request)
            );
            verify(ordemServicoRepository, never()).save(any(OrdemServico.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando servico nao encontrado")
        void shouldThrowExceptionWhenServiceNotFound() {
            var request = new CriarOrdemServicoRequest("12345678909", "ABC1234", Arrays.asList(1L), Arrays.asList(1L));
            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.of(cliente));
            when(veiculoRepository.findByPlaca(any())).thenReturn(Optional.of(veiculo));
            when(servicoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                ordemServicoApplicationService.cadastraOrdemServico(request)
            );
            verify(ordemServicoRepository, never()).save(any(OrdemServico.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando peca nao encontrada")
        void shouldThrowExceptionWhenPartNotFound() {
            var request = new CriarOrdemServicoRequest("12345678909", "ABC1234", Arrays.asList(1L), Arrays.asList(1L));
            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.of(cliente));
            when(veiculoRepository.findByPlaca(any())).thenReturn(Optional.of(veiculo));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            when(pecaRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                ordemServicoApplicationService.cadastraOrdemServico(request)
            );
            verify(ordemServicoRepository, never()).save(any(OrdemServico.class));
        }
    }

    @Nested
    @DisplayName("Obter Ordem de Servico")
    class ObterOrdemServicoTests {

        @Test
        @DisplayName("Deve obter ordem por ID")
        void shouldGetOrderServiceById() {
            var ordemServico = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(ordemServico));

            OrdemServicoDTO result = ordemServicoApplicationService.getOrdemServicoById(1L);

            assertNotNull(result);
            verify(ordemServicoRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lancar excecao quando ordem nao encontrada")
        void shouldThrowExceptionWhenOrderNotFound() {
            when(ordemServicoRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                ordemServicoApplicationService.getOrdemServicoById(999L)
            );
            verify(ordemServicoRepository).findById(999L);
        }

        @Test
        @DisplayName("Deve obter todas as ordens")
        void shouldGetAllOrderServices() {
            var ordem1 = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));
            var ordem2 = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));
            when(ordemServicoRepository.findAll()).thenReturn(Arrays.asList(ordem1, ordem2));

            List<OrdemServicoDTO> result = ordemServicoApplicationService.getAllOrdensServicos();

            assertEquals(2, result.size());
            verify(ordemServicoRepository).findAll();
        }

        @Test
        @DisplayName("Deve obter ordens por cliente")
        void shouldGetOrdersByClient() {
            var ordem = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));
            when(ordemServicoRepository.findByClienteId(1L)).thenReturn(Arrays.asList(ordem));

            List<OrdemServicoDTO> result = ordemServicoApplicationService.getOrdensServicoByCliente(1L);

            assertEquals(1, result.size());
            verify(ordemServicoRepository).findByClienteId(1L);
        }
    }

    @Nested
    @DisplayName("Avancar Status da Ordem")
    class AvancaStatusTests {

        @Test
        @DisplayName("Deve avancar status da ordem com sucesso")
        void shouldAdvanceOrderStatus() {
            var ordemServico = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(ordemServico));

            ordemServicoApplicationService.avancaStatusDaOrdemDeServico(1L);

            verify(ordemServicoRepository).findById(1L);
            verify(domainService).avancaStatus(ordemServico);
            verify(ordemServicoRepository).save(ordemServico);
        }

        @Test
        @DisplayName("Deve lancar excecao ao avancar status de ordem inexistente")
        void shouldThrowExceptionWhenOrderNotFound() {
            when(ordemServicoRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                ordemServicoApplicationService.avancaStatusDaOrdemDeServico(999L)
            );
            verify(domainService, never()).avancaStatus(any(OrdemServico.class));
            verify(ordemServicoRepository, never()).save(any(OrdemServico.class));
        }
    }

    @Nested
    @DisplayName("Aprovar Orcamento")
    class AprovarOrcamentoTests {

        @Test
        @DisplayName("Deve aprovar orcamento com sucesso")
        void shouldApproveOrcamentoSuccessfully() {
            var ordemServico = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));
            ordemServico.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);
            var request = new AprovarOrcamentoRequest(true, "OK");
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(ordemServico));
            when(ordemServicoRepository.save(any(OrdemServico.class))).thenReturn(ordemServico);

            OrdemServicoDTO result = ordemServicoApplicationService.aprovarOrcamento(1L, request);

            assertNotNull(result);
            verify(ordemServicoRepository).findById(1L);
            verify(domainService).aprovarOrcamento(ordemServico, true);
            verify(ordemServicoRepository).save(ordemServico);
        }

        @Test
        @DisplayName("Deve recusar orcamento com sucesso")
        void shouldRejectOrcamentoSuccessfully() {
            var ordemServico = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));
            ordemServico.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);
            var request = new AprovarOrcamentoRequest(false, "Muito caro");
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(ordemServico));
            when(ordemServicoRepository.save(any(OrdemServico.class))).thenReturn(ordemServico);

            OrdemServicoDTO result = ordemServicoApplicationService.aprovarOrcamento(1L, request);

            assertNotNull(result);
            verify(ordemServicoRepository).findById(1L);
            verify(domainService).aprovarOrcamento(ordemServico, false);
            verify(ordemServicoRepository).save(ordemServico);
        }

        @Test
        @DisplayName("Deve lancar excecao ao aprovar ordem inexistente")
        void shouldThrowExceptionWhenOrderNotFoundForApproval() {
            var request = new AprovarOrcamentoRequest(true, null);
            when(ordemServicoRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                ordemServicoApplicationService.aprovarOrcamento(999L, request)
            );
            verify(domainService, never()).aprovarOrcamento(any(OrdemServico.class), anyBoolean());
        }
    }

    @Nested
    @DisplayName("Deletar Ordem de Servico")
    class DeletarOrdemServicoTests {

        @Test
        @DisplayName("Deve deletar ordem com sucesso")
        void shouldDeleteOrderService() {
            var ordemServico = new OrdemServico(cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca));
            when(ordemServicoRepository.findById(1L)).thenReturn(Optional.of(ordemServico));
            ordemServicoApplicationService.deletaOrdemServico(1L);
            verify(ordemServicoRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lancar excecao ao deletar ordem inexistente")
        void shouldThrowExceptionWhenOrderNotFoundForDelete() {
            when(ordemServicoRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () ->
                ordemServicoApplicationService.deletaOrdemServico(999L)
            );
        }
    }
}
