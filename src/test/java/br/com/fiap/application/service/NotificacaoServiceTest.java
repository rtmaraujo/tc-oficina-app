package br.com.fiap.application.service;

import br.com.fiap.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacaoService - Testes de Aplicacao")
class NotificacaoServiceTest {

    @InjectMocks
    private NotificacaoService notificacaoService;

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoServiceTest.class);

    private OrdemServico criarOrdemServicoParaTeste() {
        var cliente = new Cliente(1L, "John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
        var placa = new Placa("ABC1234");
        var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);
        var servico = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
        var peca = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

        return new OrdemServico(1L, cliente, veiculo, Collections.singletonList(servico), Collections.singletonList(peca), LocalDateTime.now());
    }

    @Nested
    @DisplayName("Notificação de Aprovação de Orçamento")
    class AprovacaoOrcamentoTests {

        @Test
        @DisplayName("Deve notificar aprovação de orçamento com sucesso")
        void shouldNotifyAprovacaoOrcamentoComSucesso() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarAprovacaoOrcamento(ordem, true, null));
        }

        @Test
        @DisplayName("Deve notificar aprovação de orçamento com observações")
        void shouldNotifyAprovacaoOrcamentoComObservacoes() {
            var ordem = criarOrdemServicoParaTeste();
            var observacoes = "Cliente solicitou agilidade na execução";

            assertDoesNotThrow(() -> notificacaoService.notificarAprovacaoOrcamento(ordem, true, observacoes));
        }

        @Test
        @DisplayName("Deve notificar recusa de orçamento com sucesso")
        void shouldNotifyRecusaOrcamentoComSucesso() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarAprovacaoOrcamento(ordem, false, null));
        }

        @Test
        @DisplayName("Deve notificar recusa de orçamento com observações")
        void shouldNotifyRecusaOrcamentoComObservacoes() {
            var ordem = criarOrdemServicoParaTeste();
            var observacoes = "Orçamento acima do valor disponível";

            assertDoesNotThrow(() -> notificacaoService.notificarAprovacaoOrcamento(ordem, false, observacoes));
        }

        @Test
        @DisplayName("Deve notificar aprovação com observações em branco")
        void shouldNotifyAprovacaoComObservacoesBranco() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarAprovacaoOrcamento(ordem, true, "   "));
        }

        @Test
        @DisplayName("Deve notificar aprovação com observações vazias")
        void shouldNotifyAprovacaoComObservacoesVazias() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarAprovacaoOrcamento(ordem, true, ""));
        }
    }

    @Nested
    @DisplayName("Notificação de Atualização de Status")
    class AtualizacaoStatusTests {

        @Test
        @DisplayName("Deve notificar atualização de status com sucesso")
        void shouldNotifyAtualizacaoStatusComSucesso() {
            var ordem = criarOrdemServicoParaTeste();
            var statusAnterior = "RECEBIDA";
            var statusNovo = "AGUARDANDO_APROVACAO";

            assertDoesNotThrow(() -> notificacaoService.notificarAtualizacaoStatus(ordem, statusAnterior, statusNovo));
        }

        @Test
        @DisplayName("Deve notificar mudança de status para APROVADA")
        void shouldNotifyMudancaParaAprovada() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarAtualizacaoStatus(ordem, "AGUARDANDO_APROVACAO", "APROVADA"));
        }

        @Test
        @DisplayName("Deve notificar mudança de status para EM_EXECUCAO")
        void shouldNotifyMudancaParaEmExecucao() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarAtualizacaoStatus(ordem, "APROVADA", "EM_EXECUCAO"));
        }

        @Test
        @DisplayName("Deve notificar mudança de status para ENTREGUE")
        void shouldNotifyMudancaParaEntregue() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarAtualizacaoStatus(ordem, "EM_EXECUCAO", "ENTREGUE"));
        }

        @Test
        @DisplayName("Deve notificar mudança de status para CANCELADA")
        void shouldNotifyMudancaParaCancelada() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarAtualizacaoStatus(ordem, "EM_EXECUCAO", "CANCELADA"));
        }
    }

    @Nested
    @DisplayName("Notificação de Criação de Ordem")
    class CriacaoOrdemTests {

        @Test
        @DisplayName("Deve notificar criação de ordem com sucesso")
        void shouldNotifyOrdemCriadaComSucesso() {
            var ordem = criarOrdemServicoParaTeste();

            assertDoesNotThrow(() -> notificacaoService.notificarOrdemCriada(ordem));
        }

        @Test
        @DisplayName("Deve notificar criação de ordem com múltiplos serviços")
        void shouldNotifyOrdemCriadaComMultiplosServicos() {
            var cliente = new Cliente(1L, "John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
            var placa = new Placa("ABC1234");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);
            var servico1 = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            var servico2 = new Servico(2L, "Alinhamento", "Alinhamento de rodas", new BigDecimal("200.00"));
            var peca = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            var ordem = new OrdemServico(1L, cliente, veiculo, java.util.Arrays.asList(servico1, servico2), Collections.singletonList(peca), LocalDateTime.now());

            assertDoesNotThrow(() -> notificacaoService.notificarOrdemCriada(ordem));
        }

        @Test
        @DisplayName("Deve notificar criação de ordem com múltiplas peças")
        void shouldNotifyOrdemCriadaComMultiplasPecas() {
            var cliente = new Cliente(1L, "Jane Doe", new CpfCnpj("98765432100"), "jane@example.com", "11987654321");
            var placa = new Placa("XYZ9876");
            var veiculo = new Veiculo(2L, placa, "Honda", "Civic", 2021, cliente);
            var servico = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            var peca1 = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);
            var peca2 = new Peca(2L, "Óleo Motor", "Óleo Sintético 5W30", new BigDecimal("150.50"), 25);

            var ordem = new OrdemServico(2L, cliente, veiculo, Collections.singletonList(servico), java.util.Arrays.asList(peca1, peca2), LocalDateTime.now());

            assertDoesNotThrow(() -> notificacaoService.notificarOrdemCriada(ordem));
        }

        @Test
        @DisplayName("Deve notificar criação de ordem com cliente pessoa jurídica")
        void shouldNotifyOrdemCriadaComClienteEmpresa() {
            var cliente = new Cliente(2L, "Tech Company", new CpfCnpj("11222333000181"), "contact@tech.com", "1133334444");
            var placa = new Placa("ABC1234");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);
            var servico = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            var ordem = new OrdemServico(1L, cliente, veiculo, Collections.singletonList(servico), Collections.emptyList(), LocalDateTime.now());

            assertDoesNotThrow(() -> notificacaoService.notificarOrdemCriada(ordem));
        }
    }

    @Nested
    @DisplayName("Testes de Dados de Ordem")
    class OrdemDataTests {

        @Test
        @DisplayName("Deve lidar com ordem com diferentes estatuses")
        void shouldHandleOrdemComDiferentesEstatuses() {
            var cliente = new Cliente(1L, "John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
            var placa = new Placa("ABC1234");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);
            var servico = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            var ordem = new OrdemServico(1L, cliente, veiculo, Collections.singletonList(servico), Collections.emptyList(), LocalDateTime.now());

            assertDoesNotThrow(() -> notificacaoService.notificarOrdemCriada(ordem));
        }

        @Test
        @DisplayName("Deve lidar com ordem contendo todos os dados possíveis")
        void shouldHandleOrdemComTodosDadosPossiveis() {
            var cliente = new Cliente(1L, "John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
            var placa = new Placa("ABC1234");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);
            var servico1 = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            var servico2 = new Servico(2L, "Alinhamento", "Alinhamento de rodas", new BigDecimal("200.00"));
            var peca1 = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);
            var peca2 = new Peca(2L, "Óleo Motor", "Óleo Sintético 5W30", new BigDecimal("150.50"), 25);

            var ordem = new OrdemServico(1L, cliente, veiculo, java.util.Arrays.asList(servico1, servico2), java.util.Arrays.asList(peca1, peca2), LocalDateTime.now());

            assertDoesNotThrow(() -> {
                notificacaoService.notificarOrdemCriada(ordem);
                notificacaoService.notificarAtualizacaoStatus(ordem, "RECEBIDA", "AGUARDANDO_APROVACAO");
                notificacaoService.notificarAprovacaoOrcamento(ordem, true, "Cliente aprovou orçamento");
            });
        }
    }
}

