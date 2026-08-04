package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderService - Testes de Métodos de Negócio")
class OrdemServicoBusinessTest {

    private Cliente createTestClient() {
        return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
    }

    private Veiculo createTestVehicle(Cliente cliente) {
        return new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2020, cliente);
    }

    private Servico createTestService(String name, BigDecimal price) {
        return new Servico(name, "Test service", price);
    }

    private Peca createTestPart(String name, BigDecimal price, int stock) {
        return new Peca(name, "Test part", price, stock);
    }

    @Nested
    @DisplayName("Métodos de Status")
    class StatusMethodsTests {

        @Test
        @DisplayName("Deve permitir avançar status quando não está finalizado")
        void shouldAllowAdvanceStatusWhenNotCompleted() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            OrdemServico order = new OrdemServico(cliente, veiculo, null, null);

            assertTrue(order.canAdvanceStatus());
            assertFalse(order.isCompleto());
            assertFalse(order.isPendenteDeAprovacao());
        }

        @Test
        @DisplayName("Deve identificar status de aprovação pendente")
        void shouldIdentifyPendingApprovalStatus() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            OrdemServico order = new OrdemServico(cliente, veiculo, null, null);

            order.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);

            assertTrue(order.isPendenteDeAprovacao());
            assertFalse(order.isCompleto());
        }

        @Test
        @DisplayName("Deve identificar quando está completado")
        void shouldIdentifyWhenCompleted() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            OrdemServico order = new OrdemServico(cliente, veiculo, null, null);

            order.atualizaStatus(OrdemServicoStatus.ENTREGUE);

            assertTrue(order.isCompleto());
            assertFalse(order.canAdvanceStatus());
        }
    }

    @Nested
    @DisplayName("Gerenciamento de Itens")
    class ItemManagementTests {

        @Test
        @DisplayName("Deve adicionar serviço corretamente")
        void shouldAddServiceCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            Servico service = createTestService("Oil Change", BigDecimal.valueOf(100));
            OrdemServico order = new OrdemServico(cliente, veiculo, null, null);

            order.addServico(service);

            assertTrue(order.getServicos().contains(service));
            assertEquals(1, order.getTotalItems());
            assertTrue(order.hasItems());
            assertEquals(BigDecimal.valueOf(100), order.getTotalServicos());
        }

        @Test
        @DisplayName("Deve adicionar peça corretamente")
        void shouldAddPartCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            Peca peca = createTestPart("Oil Filter", BigDecimal.valueOf(50), 10);
            OrdemServico order = new OrdemServico(cliente, veiculo, null, null);

            order.addPeca(peca);

            assertTrue(order.getPecas().contains(peca));
            assertEquals(1, order.getTotalItems());
            assertTrue(order.hasItems());
            assertEquals(BigDecimal.valueOf(50), order.getTotalPecas());
        }

        @Test
        @DisplayName("Deve remover serviço corretamente")
        void shouldRemoveServiceCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            Servico service = createTestService("Oil Change", BigDecimal.valueOf(100));
            List<Servico> services = new ArrayList<>(Arrays.asList(service));
            OrdemServico order = new OrdemServico(cliente, veiculo, services, null);

            order.removeServico(service);

            assertFalse(order.getServicos().contains(service));
            assertEquals(0, order.getTotalItems());
            assertFalse(order.hasItems());
        }

        @Test
        @DisplayName("Deve lançar exceção ao adicionar serviço nulo")
        void shouldThrowWhenAddingNullService() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            OrdemServico order = new OrdemServico(cliente, veiculo, null, null);

            assertThrows(IllegalArgumentException.class, () -> order.addServico(null));
        }

        @Test
        @DisplayName("Deve lançar exceção ao adicionar peça nula")
        void shouldThrowWhenAddingNullPart() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            OrdemServico order = new OrdemServico(cliente, veiculo, null, null);

            assertThrows(IllegalArgumentException.class, () -> order.addPeca(null));
        }
    }

    @Nested
    @DisplayName("Cálculos de Orçamento")
    class BudgetCalculationsTests {

        @Test
        @DisplayName("Deve calcular totais parciais corretamente")
        void shouldCalculatePartialTotalsCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = createTestVehicle(cliente);
            Peca peca1 = createTestPart("Oil Filter", BigDecimal.valueOf(30), 10);
            Peca peca2 = createTestPart("Tires", BigDecimal.valueOf(200), 5);

            List<Servico> services = Arrays.asList(createTestService("Oil Change", BigDecimal.valueOf(100)),
                    createTestService("Tire Rotation", BigDecimal.valueOf(50)));
            List<Peca> pecas = Arrays.asList(peca1, peca2);
            OrdemServico order = new OrdemServico(cliente, veiculo, services, pecas);

            assertEquals(BigDecimal.valueOf(150), order.getTotalServicos());
            assertEquals(BigDecimal.valueOf(230), order.getTotalPecas());
            assertEquals(BigDecimal.valueOf(380), order.getTotalOrcamento());
            assertEquals(4, order.getTotalItems());
        }
    }
}
