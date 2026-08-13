package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Peca - Testes de Entidade de Domínio")
class PecaTest {

    @Nested
    @DisplayName("Criação e Validação de Peça")
    class CreationTests {

        @Test
        @DisplayName("Deve criar peça com dados válidos")
        void shouldCreatePecaWithValidData() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertNotNull(peca);
            assertEquals("Bateria", peca.getNome());
            assertEquals("Bateria 60Ah", peca.getDescricao());
            assertEquals(new BigDecimal("500.00"), peca.getPreco());
            assertEquals(10, peca.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve criar peça com ID")
        void shouldCreatePecaWithId() {
            var peca = new Peca(1L, "Óleo Motor", "Óleo Sintético 5W30", new BigDecimal("150.50"), 25);

            assertNotNull(peca);
            assertEquals(1L, peca.getId());
            assertEquals("Óleo Motor", peca.getNome());
            assertEquals("Óleo Sintético 5W30", peca.getDescricao());
            assertEquals(new BigDecimal("150.50"), peca.getPreco());
            assertEquals(25, peca.getQtdEstoque());
        }
    }

    @Nested
    @DisplayName("Igualdade e Identificação")
    class EqualityTests {

        @Test
        @DisplayName("Deve ser diferente de null")
        void shouldNotEqualNull() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);
            assertNotEquals(peca, null);
        }

        @Test
        @DisplayName("Deve ser diferente de objeto de outro tipo")
        void shouldNotEqualDifferentType() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);
            assertNotEquals(peca, "Bateria");
        }

        @Test
        @DisplayName("Deve ter o mesmo hashCode para peças iguais")
        void shouldHaveSameHashCodeForEqualPecas() {
            var peca1 = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);
            var peca2 = new Peca(1L, "Óleo", "Óleo Motor", new BigDecimal("150.00"), 20);

            assertEquals(peca1.hashCode(), peca2.hashCode());
        }
    }

    @Nested
    @DisplayName("Gerenciamento de Estoque")
    class EstoqueManagementTests {

        @Test
        @DisplayName("Deve adicionar quantidade ao estoque")
        void shouldAddQuantityToEstoque() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            peca.addEstoque(5);

            assertEquals(15, peca.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve remover quantidade do estoque")
        void shouldRemoveQuantityFromEstoque() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            peca.removeEstoque(3);

            assertEquals(7, peca.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve lançar exceção ao adicionar quantidade negativa")
        void shouldThrowWhenAddingNegativeQuantity() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertThrows(IllegalArgumentException.class, () -> peca.addEstoque(-5));
        }

        @Test
        @DisplayName("Deve lançar exceção ao remover quantidade maior que o estoque")
        void shouldThrowWhenRemovingMoreThanAvailable() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertThrows(IllegalArgumentException.class, () -> peca.removeEstoque(15));
        }

        @Test
        @DisplayName("Deve lançar exceção ao remover quantidade negativa")
        void shouldThrowWhenRemovingNegativeQuantity() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertThrows(IllegalArgumentException.class, () -> peca.removeEstoque(-3));
        }
    }

    @Nested
    @DisplayName("Status de Disponibilidade")
    class DisponibilidadeTests {

        @Test
        @DisplayName("Deve indicar disponibilidade quando estoque > 0")
        void shouldIndicateDisponibilidadeWhenEstoqueGreaterThanZero() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertTrue(peca.isDisponivel());
            assertFalse(peca.isForaDeEstoque());
        }

        @Test
        @DisplayName("Deve indicar falta de estoque quando estoque = 0")
        void shouldIndicateForaDeEstoqueWhenEstoqueZero() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 0);

            assertFalse(peca.isDisponivel());
            assertTrue(peca.isForaDeEstoque());
        }

        @Test
        @DisplayName("Deve indicar baixo estoque quando 0 < estoque <= 5")
        void shouldIndicateBaixoEstoque() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 3);

            assertTrue(peca.isBaixoEstoque());
            assertFalse(peca.isForaDeEstoque());
        }

        @Test
        @DisplayName("Deve indicar estoque normal quando estoque > 5")
        void shouldIndicateEstoqueNormal() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertFalse(peca.isBaixoEstoque());
            assertTrue(peca.isDisponivel());
        }
    }

    @Nested
    @DisplayName("Validação de Pedidos")
    class ValidacaoPedidosTests {

        @Test
        @DisplayName("Deve cumprir pedido quando estoque é suficiente")
        void shouldFulfillOrderWhenSufficientStock() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertTrue(peca.podeCumprirPedido(5));
            assertTrue(peca.podeCumprirPedido(10));
        }

        @Test
        @DisplayName("Deve negar pedido quando estoque é insuficiente")
        void shouldDenyOrderWhenInsufficientStock() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertFalse(peca.podeCumprirPedido(15));
        }

        @Test
        @DisplayName("Deve negar pedido quando estoque é zero")
        void shouldDenyOrderWhenNoStock() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 0);

            assertFalse(peca.podeCumprirPedido(1));
        }
    }

    @Nested
    @DisplayName("Cálculo de Valor Total")
    class ValorTotalTests {

        @Test
        @DisplayName("Deve calcular valor total corretamente")
        void shouldCalculateValorTotalCorrectly() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertEquals(new BigDecimal("5000.00"), peca.getValorTotal());
        }

        @Test
        @DisplayName("Deve retornar zero quando estoque é zero")
        void shouldReturnZeroWhenEstoqueIsZero() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 0);

            assertEquals(new BigDecimal("0.00"), peca.getValorTotal());
        }

        @Test
        @DisplayName("Deve calcular valor total com preço decimal")
        void shouldCalculateValorTotalWithDecimalPrice() {
            var peca = new Peca("Óleo Motor", "Óleo Sintético 5W30", new BigDecimal("150.50"), 5);

            assertEquals(new BigDecimal("752.50"), peca.getValorTotal());
        }
    }

    @Nested
    @DisplayName("Atualização de Informações")
    class AtualizacaoInfoTests {

        @Test
        @DisplayName("Deve atualizar informações da peça com sucesso")
        void shouldUpdatePecaInfoSuccessfully() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            peca.atualizaPecaInfo("Bateria Nova", "Bateria 75Ah", new BigDecimal("600.00"));

            assertEquals("Bateria Nova", peca.getNome());
            assertEquals("Bateria 75Ah", peca.getDescricao());
            assertEquals(new BigDecimal("600.00"), peca.getPreco());
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com nome nulo")
        void shouldThrowWhenUpdatingWithNullName() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertThrows(IllegalArgumentException.class, () ->
                peca.atualizaPecaInfo(null, "Bateria 75Ah", new BigDecimal("600.00"))
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com preço negativo")
        void shouldThrowWhenUpdatingWithNegativePrice() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            assertThrows(IllegalArgumentException.class, () ->
                peca.atualizaPecaInfo("Bateria", "Bateria 60Ah", new BigDecimal("-100.00"))
            );
        }

        @Test
        @DisplayName("Deve atualizar com descrição nula")
        void shouldUpdateWithNullDescription() {
            var peca = new Peca("Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            peca.atualizaPecaInfo("Bateria", null, new BigDecimal("600.00"));

            assertEquals("Bateria", peca.getNome());
            assertNull(peca.getDescricao());
            assertEquals(new BigDecimal("600.00"), peca.getPreco());
        }
    }
}

