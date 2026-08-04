package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Part - Testes de Métodos de Negócio")
class PecaBusinessTest {

    @Nested
    @DisplayName("Atualização de Informações")
    class InfoUpdateTests {

        @Test
        @DisplayName("Deve atualizar informações da peça corretamente")
        void shouldUpdatePartInfoCorrectly() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            peca.atualizaPecaInfo("Premium Oil Filter", "High quality filter", BigDecimal.valueOf(75));

            assertEquals("Premium Oil Filter", peca.getNome());
            assertEquals("High quality filter", peca.getDescricao());
            assertEquals(BigDecimal.valueOf(75), peca.getPreco());
        }

        @Test
        @DisplayName("Deve lançar exceção para nome nulo")
        void shouldThrowForNullName() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            assertThrows(IllegalArgumentException.class, () ->
                peca.atualizaPecaInfo(null, "Description", BigDecimal.valueOf(75))
            );
        }

        @Test
        @DisplayName("Deve lançar exceção para preço zero")
        void shouldThrowForZeroPrice() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            assertThrows(IllegalArgumentException.class, () ->
                peca.atualizaPecaInfo("New Filter", "Description", BigDecimal.ZERO)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção para preço negativo")
        void shouldThrowForNegativePrice() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            assertThrows(IllegalArgumentException.class, () ->
                peca.atualizaPecaInfo("New Filter", "Description", BigDecimal.valueOf(-10))
            );
        }
    }

    @Nested
    @DisplayName("Controle de Estoque")
    class StockControlTests {

        @Test
        @DisplayName("Deve aumentar estoque corretamente")
        void shouldIncreaseStockCorrectly() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            peca.addEstoque(5);

            assertEquals(15, peca.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve diminuir estoque corretamente")
        void shouldDecreaseStockCorrectly() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            peca.removeEstoque(3);

            assertEquals(7, peca.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve lançar exceção ao aumentar estoque com quantidade negativa")
        void shouldThrowForNegativeIncrease() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            assertThrows(IllegalArgumentException.class, () ->
                peca.addEstoque(-5)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao diminuir estoque além do disponível")
        void shouldThrowForInsufficientStock() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 5);

            assertThrows(IllegalArgumentException.class, () ->
                peca.removeEstoque(10)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao diminuir estoque com quantidade negativa")
        void shouldThrowForNegativeDecrease() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            assertThrows(IllegalArgumentException.class, () ->
                peca.removeEstoque(-3)
            );
        }
    }

    @Nested
    @DisplayName("Verificações de Disponibilidade")
    class AvailabilityChecksTests {

        @Test
        @DisplayName("Deve identificar peça disponível")
        void shouldIdentifyAvailablePart() {
            Peca availablePeca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);
            Peca unavailablePeca = new Peca("Rare Part", "Hard to find", BigDecimal.valueOf(500), 0);

            assertTrue(availablePeca.isDisponivel());
            assertFalse(unavailablePeca.isDisponivel());
        }

        @Test
        @DisplayName("Deve identificar estoque baixo")
        void shouldIdentifyLowStock() {
            Peca lowStockPeca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 3);
            Peca normalStockPeca = new Peca("Tires", "Good tires", BigDecimal.valueOf(200), 10);
            Peca outOfStockPeca = new Peca("Rare Part", "Hard to find", BigDecimal.valueOf(500), 0);

            assertTrue(lowStockPeca.isBaixoEstoque());
            assertFalse(normalStockPeca.isBaixoEstoque());
            assertFalse(outOfStockPeca.isBaixoEstoque());
        }

        @Test
        @DisplayName("Deve identificar peça esgotada")
        void shouldIdentifyOutOfStock() {
            Peca outOfStockPeca = new Peca("Rare Part", "Hard to find", BigDecimal.valueOf(500), 0);
            Peca availablePeca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            assertTrue(outOfStockPeca.isForaDeEstoque());
            assertFalse(availablePeca.isForaDeEstoque());
        }

        @Test
        @DisplayName("Deve verificar se pode atender pedido")
        void shouldCheckOrderFulfillment() {
            Peca availablePeca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);
            Peca lowStockPeca = new Peca("Tires", "Good tires", BigDecimal.valueOf(200), 2);

            assertTrue(availablePeca.podeCumprirPedido(5));
            assertTrue(lowStockPeca.podeCumprirPedido(2));
            assertFalse(lowStockPeca.podeCumprirPedido(5));
        }
    }

    @Nested
    @DisplayName("Cálculos de Valor")
    class ValueCalculationsTests {

        @Test
        @DisplayName("Deve calcular valor total do estoque corretamente")
        void shouldCalculateTotalValueCorrectly() {
            Peca peca = new Peca("Oil Filter", "Original filter", BigDecimal.valueOf(50), 10);

            BigDecimal expectedTotal = BigDecimal.valueOf(50).multiply(BigDecimal.valueOf(10));
            assertEquals(expectedTotal, peca.getValorTotal());
        }

        @Test
        @DisplayName("Deve calcular valor zero para estoque vazio")
        void shouldCalculateZeroValueForEmptyStock() {
            Peca peca = new Peca("Rare Part", "Hard to find", BigDecimal.valueOf(500), 0);

            assertEquals(BigDecimal.ZERO, peca.getValorTotal());
        }
    }
}
