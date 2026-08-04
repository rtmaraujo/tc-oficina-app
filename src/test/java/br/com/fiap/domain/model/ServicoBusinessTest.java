package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceEntity - Testes de Métodos de Negócio")
class ServicoBusinessTest {

    @Nested
    @DisplayName("Atualização de Informações")
    class InfoUpdateTests {

        @Test
        @DisplayName("Deve atualizar informações do serviço corretamente")
        void shouldUpdateServiceInfoCorrectly() {
            Servico service = new Servico("Oil Change", "Basic oil change", BigDecimal.valueOf(100));

            service.atualizaServicoInfo("Premium Oil Change", "Complete oil change with filter", BigDecimal.valueOf(150));

            assertEquals("Premium Oil Change", service.getNome());
            assertEquals("Complete oil change with filter", service.getDescricao());
            assertEquals(BigDecimal.valueOf(150), service.getPreco());
        }

        @Test
        @DisplayName("Deve lançar exceção para nome nulo")
        void shouldThrowForNullName() {
            Servico service = new Servico("Oil Change", "Basic oil change", BigDecimal.valueOf(100));

            assertThrows(IllegalArgumentException.class, () ->
                service.atualizaServicoInfo(null, "Description", BigDecimal.valueOf(150))
            );
        }

        @Test
        @DisplayName("Deve lançar exceção para preço zero")
        void shouldThrowForZeroPrice() {
            Servico service = new Servico("Oil Change", "Basic oil change", BigDecimal.valueOf(100));

            assertThrows(IllegalArgumentException.class, () ->
                service.atualizaServicoInfo("New Service", "Description", BigDecimal.ZERO)
            );
        }
    }

    @Nested
    @DisplayName("Classificação por Preço")
    class PriceClassificationTests {

        @Test
        @DisplayName("Deve identificar serviço caro corretamente")
        void shouldIdentifyExpensiveServiceCorrectly() {
            Servico expensiveService = new Servico("Engine Repair", "Major repair", BigDecimal.valueOf(800));
            Servico cheapService = new Servico("Oil Change", "Basic service", BigDecimal.valueOf(100));

            assertTrue(expensiveService.isAcimaDe500());
            assertFalse(cheapService.isAcimaDe500());
        }

        @Test
        @DisplayName("Deve identificar serviço barato corretamente")
        void shouldIdentifyCheapServiceCorrectly() {
            Servico cheapService = new Servico("Tire Pressure Check", "Quick check", BigDecimal.valueOf(20));
            Servico normalService = new Servico("Oil Change", "Basic service", BigDecimal.valueOf(100));

            assertTrue(cheapService.isAbaixoDe50());
            assertFalse(normalService.isAbaixoDe50());
        }
    }

    @Nested
    @DisplayName("Categorização de Tipo de Serviço")
    class ServiceTypeTests {

        @Test
        @DisplayName("Deve categorizar serviço de substituição corretamente")
        void shouldCategorizeReplacementServiceCorrectly() {
            Servico replacementService = new Servico("Troca de Óleo", "Oil change", BigDecimal.valueOf(100));
            Servico otherService = new Servico("Lavagem", "Car wash", BigDecimal.valueOf(50));

            assertEquals("REPLACEMENT", replacementService.getTipoDeServico());
            assertTrue(replacementService.isTipoSubstituicao());
            assertFalse(replacementService.isTipoManutencao());

            assertEquals("CLEANING", otherService.getTipoDeServico());
        }

        @Test
        @DisplayName("Deve categorizar serviço de manutenção corretamente")
        void shouldCategorizeMaintenanceServiceCorrectly() {
            Servico maintenanceService = new Servico("Revisão Completa", "Full maintenance", BigDecimal.valueOf(500));
            Servico diagnosticService = new Servico("Diagnóstico", "Problem diagnosis", BigDecimal.valueOf(150));

            assertEquals("MAINTENANCE", maintenanceService.getTipoDeServico());
            assertTrue(maintenanceService.isTipoManutencao());
            assertFalse(maintenanceService.isTipoSubstituicao());

            assertEquals("DIAGNOSTIC", diagnosticService.getTipoDeServico());
        }

        @Test
        @DisplayName("Deve categorizar serviço geral corretamente")
        void shouldCategorizeGeneralServiceCorrectly() {
            Servico generalService = new Servico("Serviço Especial", "Special service", BigDecimal.valueOf(200));

            assertEquals("GENERAL", generalService.getTipoDeServico());
            assertFalse(generalService.isTipoManutencao());
            assertFalse(generalService.isTipoSubstituicao());
        }

        @Test
        @DisplayName("Deve ser case insensitive na categorização")
        void shouldBeCaseInsensitiveInCategorization() {
            Servico service = new Servico("TROCA DE PNEUS", "Tire replacement", BigDecimal.valueOf(300));

            assertEquals("REPLACEMENT", service.getTipoDeServico());
            assertTrue(service.isTipoSubstituicao());
        }
    }
}
