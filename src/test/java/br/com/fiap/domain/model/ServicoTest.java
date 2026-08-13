package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Servico - Testes de Entidade de Domínio")
class ServicoTest {

    @Nested
    @DisplayName("Criação e Validação de Serviço")
    class CreationTests {

        @Test
        @DisplayName("Deve criar serviço com dados válidos")
        void shouldCreateServicoWithValidData() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            assertNotNull(servico);
            assertEquals("Troca de Óleo", servico.getNome());
            assertEquals("Troca de óleo do motor", servico.getDescricao());
            assertEquals(new BigDecimal("150.00"), servico.getPreco());
        }

        @Test
        @DisplayName("Deve criar serviço com ID")
        void shouldCreateServicoWithId() {
            var servico = new Servico(1L, "Alinhamento", "Alinhamento de rodas", new BigDecimal("200.00"));

            assertNotNull(servico);
            assertEquals(1L, servico.getId());
            assertEquals("Alinhamento", servico.getNome());
            assertEquals("Alinhamento de rodas", servico.getDescricao());
            assertEquals(new BigDecimal("200.00"), servico.getPreco());
        }
    }

    @Nested
    @DisplayName("Igualdade e Identificação")
    class EqualityTests {

        @Test
        @DisplayName("Deve ser diferente de null")
        void shouldNotEqualNull() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            assertNotEquals(servico, null);
        }

        @Test
        @DisplayName("Deve ser diferente de objeto de outro tipo")
        void shouldNotEqualDifferentType() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            assertNotEquals(servico, "Troca de Óleo");
        }

        @Test
        @DisplayName("Deve ter o mesmo hashCode para serviços iguais")
        void shouldHaveSameHashCodeForEqualServicos() {
            var servico1 = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            var servico2 = new Servico(1L, "Alinhamento", "Alinhamento de rodas", new BigDecimal("200.00"));

            assertEquals(servico1.hashCode(), servico2.hashCode());
        }
    }

    @Nested
    @DisplayName("Classificação de Preços")
    class PrecoTests {

        @Test
        @DisplayName("Deve identificar serviço acima de 500")
        void shouldIdentifyServicoAbove500() {
            var servico = new Servico("Diagnóstico", "Diagnóstico completo", new BigDecimal("600.00"));

            assertTrue(servico.isAcimaDe500());
            assertFalse(servico.isAbaixoDe50());
        }

        @Test
        @DisplayName("Deve identificar serviço abaixo de 50")
        void shouldIdentifyServicoBelow50() {
            var servico = new Servico("Verificação", "Verificação rápida", new BigDecimal("30.00"));

            assertTrue(servico.isAbaixoDe50());
            assertFalse(servico.isAcimaDe500());
        }

        @Test
        @DisplayName("Deve identificar serviço intermediário")
        void shouldIdentifyIntermediateServico() {
            var servico = new Servico("Alinhamento", "Alinhamento de rodas", new BigDecimal("200.00"));

            assertFalse(servico.isAcimaDe500());
            assertFalse(servico.isAbaixoDe50());
        }

        @Test
        @DisplayName("Deve identificar serviço exatamente em 500")
        void shouldIdentifyServicoAt500() {
            var servico = new Servico("Serviço", "Descrição", new BigDecimal("500.00"));

            assertFalse(servico.isAcimaDe500());
            assertFalse(servico.isAbaixoDe50());
        }

        @Test
        @DisplayName("Deve identificar serviço exatamente em 50")
        void shouldIdentifyServicoAt50() {
            var servico = new Servico("Serviço", "Descrição", new BigDecimal("50.00"));

            assertFalse(servico.isAcimaDe500());
            assertFalse(servico.isAbaixoDe50());
        }
    }

    @Nested
    @DisplayName("Classificação de Tipo de Serviço")
    class TipoServicoTests {

        @Test
        @DisplayName("Deve identificar serviço de REPLACEMENT (Troca)")
        void shouldIdentifyReplacementService() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            assertEquals("REPLACEMENT", servico.getTipoDeServico());
            assertTrue(servico.isTipoSubstituicao());
            assertFalse(servico.isTipoManutencao());
        }

        @Test
        @DisplayName("Deve identificar serviço de REPLACEMENT (Substituição)")
        void shouldIdentifyReplacementServiceSubstitui() {
            var servico = new Servico("Substituição de Pneu", "Substituição de pneu", new BigDecimal("100.00"));

            assertEquals("REPLACEMENT", servico.getTipoDeServico());
            assertTrue(servico.isTipoSubstituicao());
        }

        @Test
        @DisplayName("Deve identificar serviço de CLEANING (Limpeza)")
        void shouldIdentifyCleaningService() {
            var servico = new Servico("Limpeza do Motor", "Limpeza do motor", new BigDecimal("200.00"));

            assertEquals("CLEANING", servico.getTipoDeServico());
            assertFalse(servico.isTipoManutencao());
            assertFalse(servico.isTipoSubstituicao());
        }

        @Test
        @DisplayName("Deve identificar serviço de CLEANING (Lavagem)")
        void shouldIdentifyCleaningServiceWashing() {
            var servico = new Servico("Lavagem Completa", "Lavagem completa do veículo", new BigDecimal("150.00"));

            assertEquals("CLEANING", servico.getTipoDeServico());
        }

        @Test
        @DisplayName("Deve identificar serviço de DIAGNOSTIC")
        void shouldIdentifyDiagnosticService() {
            var servico = new Servico("Diagnóstico do Motor", "Diagnóstico completo", new BigDecimal("500.00"));

            assertEquals("DIAGNOSTIC", servico.getTipoDeServico());
            assertFalse(servico.isTipoManutencao());
        }

        @Test
        @DisplayName("Deve identificar serviço de DIAGNOSTIC (Verificação)")
        void shouldIdentifyDiagnosticServiceVerification() {
            var servico = new Servico("Verificação de Sistema", "Verificação de sistema", new BigDecimal("100.00"));

            assertEquals("DIAGNOSTIC", servico.getTipoDeServico());
        }

        @Test
        @DisplayName("Deve identificar serviço de MAINTENANCE (Revisão)")
        void shouldIdentifyMaintenanceServiceRevision() {
            var servico = new Servico("Revisão Preventiva", "Revisão preventiva", new BigDecimal("300.00"));

            assertEquals("MAINTENANCE", servico.getTipoDeServico());
            assertTrue(servico.isTipoManutencao());
            assertFalse(servico.isTipoSubstituicao());
        }

        @Test
        @DisplayName("Deve identificar serviço de MAINTENANCE (Manutenção)")
        void shouldIdentifyMaintenanceServiceMaintenance() {
            var servico = new Servico("Manutenção Básica", "Manutenção básica", new BigDecimal("250.00"));

            assertEquals("MAINTENANCE", servico.getTipoDeServico());
            assertTrue(servico.isTipoManutencao());
        }

        @Test
        @DisplayName("Deve identificar serviço GENERAL por padrão")
        void shouldIdentifyGeneralServiceByDefault() {
            var servico = new Servico("Serviço Diversos", "Outros serviços", new BigDecimal("100.00"));

            assertEquals("GENERAL", servico.getTipoDeServico());
            assertFalse(servico.isTipoManutencao());
            assertFalse(servico.isTipoSubstituicao());
        }
    }

    @Nested
    @DisplayName("Atualização de Informações")
    class AtualizacaoInfoTests {

        @Test
        @DisplayName("Deve atualizar informações do serviço com sucesso")
        void shouldUpdateServicoInfoSuccessfully() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            servico.atualizaServicoInfo("Troca de Óleo Premium", "Troca de óleo Premium", new BigDecimal("200.00"));

            assertEquals("Troca de Óleo Premium", servico.getNome());
            assertEquals("Troca de óleo Premium", servico.getDescricao());
            assertEquals(new BigDecimal("200.00"), servico.getPreco());
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com nome nulo")
        void shouldThrowWhenUpdatingWithNullName() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            assertThrows(IllegalArgumentException.class, () ->
                servico.atualizaServicoInfo(null, "Troca de óleo do motor", new BigDecimal("200.00"))
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com preço negativo")
        void shouldThrowWhenUpdatingWithNegativePrice() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            assertThrows(IllegalArgumentException.class, () ->
                servico.atualizaServicoInfo("Troca de Óleo", "Troca de óleo", new BigDecimal("-100.00"))
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com preço zero")
        void shouldThrowWhenUpdatingWithZeroPrice() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            assertThrows(IllegalArgumentException.class, () ->
                servico.atualizaServicoInfo("Troca de Óleo", "Troca de óleo", BigDecimal.ZERO)
            );
        }

        @Test
        @DisplayName("Deve atualizar com descrição nula")
        void shouldUpdateWithNullDescription() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            servico.atualizaServicoInfo("Troca de Óleo", null, new BigDecimal("200.00"));

            assertEquals("Troca de Óleo", servico.getNome());
            assertNull(servico.getDescricao());
            assertEquals(new BigDecimal("200.00"), servico.getPreco());
        }
    }

    @Nested
    @DisplayName("Mutators e Acessors")
    class MutatorsTests {

        @Test
        @DisplayName("Deve definir e obter nome")
        void shouldSetAndGetName() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            servico.setNome("Novo Nome");

            assertEquals("Novo Nome", servico.getNome());
        }

        @Test
        @DisplayName("Deve definir e obter descrição")
        void shouldSetAndGetDescription() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            servico.setDescricao("Nova descrição");

            assertEquals("Nova descrição", servico.getDescricao());
        }

        @Test
        @DisplayName("Deve definir e obter preço")
        void shouldSetAndGetPrice() {
            var servico = new Servico("Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            servico.setPreco(new BigDecimal("250.00"));

            assertEquals(new BigDecimal("250.00"), servico.getPreco());
        }
    }
}

