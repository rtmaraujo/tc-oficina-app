package br.com.fiap.domain.service;

import br.com.fiap.domain.model.*;
import br.com.fiap.presentation.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ordem Servico Domain - Testes de Transicao de Status")
class OrdemServicoDomainServicoTest {

    private OrdemServicoDomainService ordemServicoDomainService;
    private OrdemServico ordemServico;

    @BeforeEach
    void setUp() {
        ordemServicoDomainService = new OrdemServicoDomainService();
        
        // Criar cliente
        CpfCnpj cpf = new CpfCnpj("12345678909");
        Cliente cliente = new Cliente("João Silva", cpf, "joao@example.com", "11987654321");
        
        // Criar veículo
        Placa placa = new Placa("ABC1234");
        Veiculo veiculo = new Veiculo(placa, "Toyota", "Corolla", 2015, cliente);
        
        // Criar pedido no status inicial RECEBIDA
        ordemServico = new OrdemServico(cliente, veiculo, new ArrayList<>(), new ArrayList<>());
    }

    @Nested
    @DisplayName("Transicao de Status")
    class TransicaoStatusTests {

        @Test
        @DisplayName("Deve transicionar de RECEBIDA para EM_DIAGNOSTICO")
        void shouldAdvanceFromRecebidaToEmDiagnostico() {
            assertEquals(OrdemServicoStatus.RECEBIDA, ordemServico.getStatus());
            
            ordemServicoDomainService.avancaStatus(ordemServico);
            
            assertEquals(OrdemServicoStatus.EM_DIAGNOSTICO, ordemServico.getStatus());
        }

        @Test
        @DisplayName("Deve transicionar de EM_DIAGNOSTICO para AGUARDANDO_APROVACAO")
        void shouldAdvanceFromEmDiagnosticoToAguardandoAprovacao() {
            ordemServico.atualizaStatus(OrdemServicoStatus.EM_DIAGNOSTICO);
            
            ordemServicoDomainService.avancaStatus(ordemServico);
            
            assertEquals(OrdemServicoStatus.AGUARDANDO_APROVACAO, ordemServico.getStatus());
        }

        @Test
        @DisplayName("Nao deve avancar de AGUARDANDO_APROVACAO - deve usar aprovar-orcamento")
        void shouldNotAdvanceFromAguardandoAprovacao() {
            ordemServico.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);

            assertThrows(ValidationException.class, () ->
                ordemServicoDomainService.avancaStatus(ordemServico)
            );
            assertEquals(OrdemServicoStatus.AGUARDANDO_APROVACAO, ordemServico.getStatus());
        }

        @Test
        @DisplayName("Deve transicionar de EM_EXECUCAO para FINALIZADA")
        void shouldAdvanceFromEmExecucaoToFinalizada() {
            ordemServico.atualizaStatus(OrdemServicoStatus.EM_EXECUCAO);
            
            ordemServicoDomainService.avancaStatus(ordemServico);
            
            assertEquals(OrdemServicoStatus.FINALIZADA, ordemServico.getStatus());
        }

        @Test
        @DisplayName("Deve transicionar de FINALIZADA para ENTREGUE")
        void shouldAdvanceFromFinalizadaToEntregue() {
            ordemServico.atualizaStatus(OrdemServicoStatus.FINALIZADA);
            
            ordemServicoDomainService.avancaStatus(ordemServico);
            
            assertEquals(OrdemServicoStatus.ENTREGUE, ordemServico.getStatus());
        }

        @Test
        @DisplayName("Nao deve transicionar alem de ENTREGUE e deve lancar excecao")
        void shouldNotAdvanceBeyondEntregue() {
            ordemServico.atualizaStatus(OrdemServicoStatus.ENTREGUE);

            assertThrows(ValidationException.class, () ->
                ordemServicoDomainService.avancaStatus(ordemServico)
            );
            assertEquals(OrdemServicoStatus.ENTREGUE, ordemServico.getStatus());
        }
    }

    @Nested
    @DisplayName("Ciclo Completo de Transicoes")
    class CicloCompletoTests {

        @Test
        @DisplayName("Deve completar ciclo completo RECEBIDA -> ENTREGUE")
        void shouldCompleteCycleThroughAllStates() {
            assertEquals(OrdemServicoStatus.RECEBIDA, ordemServico.getStatus());

            ordemServicoDomainService.avancaStatus(ordemServico);
            assertEquals(OrdemServicoStatus.EM_DIAGNOSTICO, ordemServico.getStatus());

            ordemServicoDomainService.avancaStatus(ordemServico);
            assertEquals(OrdemServicoStatus.AGUARDANDO_APROVACAO, ordemServico.getStatus());

            ordemServicoDomainService.aprovarOrcamento(ordemServico, true);
            assertEquals(OrdemServicoStatus.EM_EXECUCAO, ordemServico.getStatus());

            ordemServicoDomainService.avancaStatus(ordemServico);
            assertEquals(OrdemServicoStatus.FINALIZADA, ordemServico.getStatus());

            ordemServicoDomainService.avancaStatus(ordemServico);
            assertEquals(OrdemServicoStatus.ENTREGUE, ordemServico.getStatus());
        }
    }

    @Nested
    @DisplayName("Validacoes de Estado")
    class ValidacoesEstadoTests {

        @Test
        @DisplayName("Deve permitir avanco enquanto nao estiver ENTREGUE")
        void shouldAllowAdvanceWhenNotCompleted() {
            ordemServico.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);
            
            assertTrue(ordemServico.canAdvanceStatus());
        }

        @Test
        @DisplayName("Nao deve permitir avanco quando estiver ENTREGUE")
        void shouldNotAllowAdvanceWhenCompleted() {
            ordemServico.atualizaStatus(OrdemServicoStatus.ENTREGUE);
            
            assertFalse(ordemServico.canAdvanceStatus());
        }

        @Test
        @DisplayName("Deve reconhecer quando pedido foi completado")
        void shouldRecognizeCompletedOrder() {
            assertFalse(ordemServico.isCompleto());
            
            ordemServico.atualizaStatus(OrdemServicoStatus.ENTREGUE);
            assertTrue(ordemServico.isCompleto());
        }

        @Test
        @DisplayName("Deve reconhecer quando pedido esta aguardando aprovacao")
        void shouldRecognizePendingApprovalStatus() {
            assertFalse(ordemServico.isPendenteDeAprovacao());
            
            ordemServico.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);
            assertTrue(ordemServico.isPendenteDeAprovacao());
        }
    }

    @Nested
    @DisplayName("Aprovacao de Orcamento")
    class AprovacaoOrcamentoTests {

        @Test
        @DisplayName("Deve aprovar orcamento e transicionar para EM_EXECUCAO")
        void shouldApproveOrcamentoAndTransitionToEmExecucao() {
            ordemServico.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);

            ordemServicoDomainService.aprovarOrcamento(ordemServico, true);

            assertEquals(OrdemServicoStatus.EM_EXECUCAO, ordemServico.getStatus());
        }

        @Test
        @DisplayName("Deve recusar orcamento e transicionar para RECUSADA")
        void shouldRejectOrcamentoAndTransitionToRecusada() {
            ordemServico.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);

            ordemServicoDomainService.aprovarOrcamento(ordemServico, false);

            assertEquals(OrdemServicoStatus.RECUSADA, ordemServico.getStatus());
        }

        @Test
        @DisplayName("Deve lançar excecao ao aprovar quando nao estiver em AGUARDANDO_APROVACAO")
        void shouldThrowExceptionWhenNotInAguardandoAprovacao() {
            ordemServico.atualizaStatus(OrdemServicoStatus.EM_DIAGNOSTICO);

            assertThrows(ValidationException.class, () ->
                ordemServicoDomainService.aprovarOrcamento(ordemServico, true)
            );
        }
    }
}

