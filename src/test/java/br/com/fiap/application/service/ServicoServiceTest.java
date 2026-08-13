package br.com.fiap.application.service;

import br.com.fiap.domain.model.Servico;
import br.com.fiap.domain.repository.ServicoRepository;
import br.com.fiap.presentation.dto.request.AtualizarServicoRequest;
import br.com.fiap.presentation.dto.request.CriarServicoRequest;
import br.com.fiap.presentation.dto.response.ServicoDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServicoService - Testes de Aplicacao")
class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private Servico servico;

    @BeforeEach
    void setUp() {
        servico = new Servico("Troca de Oleo", "Troca completa de oleo sintetizado", new BigDecimal("150.00"));
    }

    @Nested
    @DisplayName("Criar Servico")
    class CriarServicoTests {

        @Test
        @DisplayName("Deve criar servico com dados validos")
        void shouldCreateService() {
            var request = new CriarServicoRequest("Troca de Oleo", "Troca completa", new BigDecimal("150.00"));
            when(servicoRepository.save(any(Servico.class))).thenReturn(servico);

            ServicoDTO resultado = servicoService.cadastraServico(request);

            assertNotNull(resultado);
            assertEquals("Troca de Oleo", resultado.nome());
            assertEquals(new BigDecimal("150.00"), resultado.preco());
            verify(servicoRepository).save(any(Servico.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando preco invalido")
        void shouldThrowExceptionForInvalidPrice() {
            var request = new CriarServicoRequest("Servico", "Desc", BigDecimal.ZERO);
            assertThrows(ValidationException.class, () ->
                servicoService.cadastraServico(request)
            );
        }
    }

    @Nested
    @DisplayName("Obter Servico")
    class ObterServicoTests {

        @Test
        @DisplayName("Deve obter servico por id")
        void shouldGetServiceById() {
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

            Optional<Servico> resultado = servicoService.getServicoById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("Troca de Oleo", resultado.get().getNome());
        }

        @Test
        @DisplayName("Deve retornar vazio quando servico nao encontrado")
        void shouldReturnEmptyWhenServiceNotFound() {
            when(servicoRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Servico> resultado = servicoService.getServicoById(999L);

            assertFalse(resultado.isPresent());
        }
    }

    @Nested
    @DisplayName("Atualizar Servico")
    class AtualizarServicoTests {

        @Test
        @DisplayName("Deve atualizar servico com dados validos")
        void shouldUpdateService() {
            var request = new AtualizarServicoRequest("Troca de Oleo Premium", "Oleo sintetico de qualidade", new BigDecimal("200.00"));
            var servicoAtualizado = new Servico("Troca de Oleo Premium", "Oleo sintetico de qualidade", new BigDecimal("200.00"));

            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            when(servicoRepository.save(any(Servico.class))).thenReturn(servicoAtualizado);

            ServicoDTO resultado = servicoService.atualizaServico(1L, request);

            assertNotNull(resultado);
            assertEquals("Troca de Oleo Premium", resultado.nome());
            assertEquals(new BigDecimal("200.00"), resultado.preco());
            verify(servicoRepository).save(any(Servico.class));
        }

        @Test
        @DisplayName("Deve lancar excecao ao atualizar servico inexistente")
        void shouldThrowExceptionWhenServiceNotFound() {
            var request = new AtualizarServicoRequest("Novo Nome", "Descricao", new BigDecimal("100"));
            when(servicoRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                servicoService.atualizaServico(999L, request)
            );
        }
    }

    @Nested
    @DisplayName("Categorizar Servico")
    class CategorizarServicoTests {

        @Test
        @DisplayName("Deve categorizar como REPLACEMENT")
        void shouldCategorizeAsReplacement() {
            Servico servicoReplacement = new Servico("Troca de Filtro", "", new BigDecimal("50"));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servicoReplacement));

            Optional<Servico> resultado = servicoService.getServicoById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("REPLACEMENT", resultado.get().getTipoDeServico());
        }

        @Test
        @DisplayName("Deve categorizar como CLEANING")
        void shouldCategorizeAsCleaning() {
            Servico servicoCleaning = new Servico("Limpeza do Motor", "", new BigDecimal("100"));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servicoCleaning));

            Optional<Servico> resultado = servicoService.getServicoById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("CLEANING", resultado.get().getTipoDeServico());
        }

        @Test
        @DisplayName("Deve categorizar como MAINTENANCE")
        void shouldCategorizeAsMaintenance() {
            Servico servicoMaintenance = new Servico("Revisão 40mil km", "", new BigDecimal("300"));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servicoMaintenance));

            Optional<Servico> resultado = servicoService.getServicoById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("MAINTENANCE", resultado.get().getTipoDeServico());
        }

        @Test
        @DisplayName("Deve categorizar como DIAGNOSTIC")
        void shouldCategorizeAsDiagnostic() {
            Servico servicoDiag = new Servico("Diagnóstico computadorizado", "", new BigDecimal("150"));
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servicoDiag));

            Optional<Servico> resultado = servicoService.getServicoById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("DIAGNOSTIC", resultado.get().getTipoDeServico());
        }
    }

    @Nested
    @DisplayName("Analise de Preco")
    class AnalisePrecosTests {

        @Test
        @DisplayName("Deve identificar servico caro")
        void shouldIdentifyExpensiveService() {
            Servico servicoCaro = new Servico("Motor Completo", "", new BigDecimal("2000"));
            assertTrue(servicoCaro.isAcimaDe500());
        }

        @Test
        @DisplayName("Deve identificar servico barato")
        void shouldIdentifyCheapService() {
            Servico servicoBarato = new Servico("Limpeza", "", new BigDecimal("30"));
            assertTrue(servicoBarato.isAbaixoDe50());
        }

        @Test
        @DisplayName("Deve identificar servico com preco intermediario")
        void shouldIdentifyMiddleRangeService() {
            Servico servicoMedio = new Servico("Alinhamento", "", new BigDecimal("120"));
            assertFalse(servicoMedio.isAcimaDe500());
            assertFalse(servicoMedio.isAbaixoDe50());
        }
    }

    @Nested
    @DisplayName("Deletar Servico")
    class DeletarServicoTests {

        @Test
        @DisplayName("Deve deletar servico com sucesso")
        void shouldDeleteService() {
            when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
            servicoService.deletaServico(1L);
            verify(servicoRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lancar excecao quando servico nao encontrado para exclusao")
        void shouldThrowExceptionWhenServiceNotFoundForDelete() {
            when(servicoRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () ->
                servicoService.deletaServico(999L)
            );
        }
    }
}
