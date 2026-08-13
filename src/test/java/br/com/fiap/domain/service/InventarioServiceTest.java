package br.com.fiap.domain.service;

import br.com.fiap.domain.model.Peca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InventoryService - Testes de Serviço de Domínio")
class InventarioServiceTest {

    private Peca createTestPart(String name, BigDecimal price, int stock) {
        return new Peca(name, "Test part", price, stock);
    }

    @Nested
    @DisplayName("Filtragem de Peças por Status")
    class PecaFilteringTests {

        @Test
        @DisplayName("Deve filtrar peças com estoque baixo")
        void shouldFilterLowStockParts() {
            InventarioService service = new InventarioService();
            Peca lowStockPeca1 = createTestPart("Part1", BigDecimal.valueOf(50), 3);
            Peca lowStockPeca2 = createTestPart("Part2", BigDecimal.valueOf(50), 1);
            Peca normalStockPeca = createTestPart("Part3", BigDecimal.valueOf(50), 10);
            Peca outOfStockPeca = createTestPart("Part4", BigDecimal.valueOf(50), 0);

            List<Peca> pecas = Arrays.asList(lowStockPeca1, lowStockPeca2, normalStockPeca, outOfStockPeca);
            List<Peca> lowStockPecas = service.getPecasBaixoEstoque(pecas);

            assertEquals(2, lowStockPecas.size());
            assertTrue(lowStockPecas.contains(lowStockPeca1));
            assertTrue(lowStockPecas.contains(lowStockPeca2));
        }
    }

    @Nested
    @DisplayName("Verificação de Atendimento de Pedidos")
    class OrderFulfillmentTests {

        @Test
        @DisplayName("Deve verificar se pode atender pedido")
        void shouldCheckIfCanFulfillOrder() {
            InventarioService service = new InventarioService();
            Peca availablePeca1 = createTestPart("Part1", BigDecimal.valueOf(50), 10);
            Peca availablePeca2 = createTestPart("Part2", BigDecimal.valueOf(50), 5);
            Peca outOfStockPeca = createTestPart("Part3", BigDecimal.valueOf(50), 0);

            List<Peca> orderPecas = Arrays.asList(availablePeca1, availablePeca2);
            List<Peca> insufficientPecas = Arrays.asList(availablePeca1, outOfStockPeca);

            assertTrue(service.podeCumprirPedido(orderPecas));
            assertFalse(service.podeCumprirPedido(insufficientPecas));
        }

        @Test
        @DisplayName("Deve processar atendimento de pedido corretamente")
        void shouldProcessOrderFulfillmentCorrectly() {
            InventarioService service = new InventarioService();
            Peca peca1 = createTestPart("Part1", BigDecimal.valueOf(50), 10);
            Peca peca2 = createTestPart("Part2", BigDecimal.valueOf(50), 5);

            List<Peca> orderPecas = Arrays.asList(peca1, peca2);
            service.processarCumprimentoDePedidos(orderPecas);

            assertEquals(9, peca1.getQtdEstoque());
            assertEquals(4, peca2.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve lançar exceção para pedido que não pode ser atendido")
        void shouldThrowExceptionForUnfulfillableOrder() {
            InventarioService service = new InventarioService();
            Peca availablePeca = createTestPart("Part1", BigDecimal.valueOf(50), 10);
            Peca outOfStockPeca = createTestPart("Part2", BigDecimal.valueOf(50), 0);

            List<Peca> orderPecas = Arrays.asList(availablePeca, outOfStockPeca);

            assertThrows(IllegalStateException.class, () ->
                service.processarCumprimentoDePedidos(orderPecas)
            );
        }
    }

    @Nested
    @DisplayName("Reposicionamento de Estoque")
    class StockRestockingTests {

        @Test
        @DisplayName("Deve reposicionar estoque corretamente")
        void shouldRestockPartsCorrectly() {
            InventarioService service = new InventarioService();
            Peca peca1 = createTestPart("Part1", BigDecimal.valueOf(50), 5);
            Peca peca2 = createTestPart("Part2", BigDecimal.valueOf(50), 2);

            List<Peca> pecas = Arrays.asList(peca1, peca2);
            service.reabastecerPecas(pecas, 10);

            assertEquals(15, peca1.getQtdEstoque());
            assertEquals(12, peca2.getQtdEstoque());
        }
    }

    @Nested
    @DisplayName("Relatório de Inventário")
    class ResumoInventarioTests {

        @Test
        @DisplayName("Deve gerar resumo de inventário corretamente")
        void shouldGenerateInventorySummaryCorrectly() {
            InventarioService service = new InventarioService();
            Peca availablePeca1 = createTestPart("Part1", BigDecimal.valueOf(50), 10);
            Peca availablePeca2 = createTestPart("Part2", BigDecimal.valueOf(50), 8);
            Peca lowStockPeca = createTestPart("Part3", BigDecimal.valueOf(50), 3);
            Peca outOfStockPeca = createTestPart("Part4", BigDecimal.valueOf(50), 0);

            List<Peca> pecas = Arrays.asList(availablePeca1, availablePeca2, lowStockPeca, outOfStockPeca);
            var summary = service.getResumoInventario(pecas);

            assertEquals(4, summary.getTotalPecas());
            assertEquals(3, summary.getPecasDisponivel()); // 10, 8, 3 são > 0
            assertEquals(1, summary.getPecasBaixoEstoque());
            assertEquals(1, summary.getPecasForaDeEstoque());
            assertEquals(75.0, summary.getPorcentagemDeDisponibilidade());
        }

        @Test
        @DisplayName("Deve calcular porcentagem de disponibilidade corretamente")
        void shouldCalculateAvailabilityPercentageCorrectly() {
            InventarioService service = new InventarioService();
            Peca availablePeca = createTestPart("Part1", BigDecimal.valueOf(50), 10);
            Peca outOfStockPeca = createTestPart("Part2", BigDecimal.valueOf(50), 0);

            List<Peca> pecas = Arrays.asList(availablePeca, outOfStockPeca);
            var summary = service.getResumoInventario(pecas);

            assertEquals(50.0, summary.getPorcentagemDeDisponibilidade());
        }

        @Test
        @DisplayName("Deve retornar zero para lista vazia")
        void shouldReturnZeroForEmptyList() {
            InventarioService service = new InventarioService();
            List<Peca> emptyPecas = Arrays.asList();
            var summary = service.getResumoInventario(emptyPecas);

            assertEquals(0, summary.getTotalPecas());
            assertEquals(0, summary.getPecasDisponivel());
            assertEquals(0.0, summary.getPorcentagemDeDisponibilidade());
        }
    }
}
