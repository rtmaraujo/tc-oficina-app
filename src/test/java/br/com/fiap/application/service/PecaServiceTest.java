package br.com.fiap.application.service;

import br.com.fiap.domain.model.Peca;
import br.com.fiap.domain.repository.PecaRepository;
import br.com.fiap.presentation.dto.request.AtualizarPecaRequest;
import br.com.fiap.presentation.dto.request.CriarPecaRequest;
import br.com.fiap.presentation.dto.response.PecaDTO;
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
@DisplayName("PecaService - Testes de Aplicacao")
class PecaServiceTest {

    @Mock
    private PecaRepository pecaRepository;

    @InjectMocks
    private PecaService pecaService;

    private Peca peca;

    @BeforeEach
    void setUp() {
        peca = new Peca("Oleo Sintetico", "5L", new BigDecimal("85.50"), 20);
    }

    @Nested
    @DisplayName("Criar Peca")
    class CriarPecaTests {

        @Test
        @DisplayName("Deve criar peca com dados validos")
        void shouldCreatePart() {
            var request = new CriarPecaRequest("Oleo Sintetico", "5L", new BigDecimal("85.50"), 20);
            when(pecaRepository.save(any(Peca.class))).thenReturn(peca);

            PecaDTO resultado = pecaService.cadastraPeca(request);

            assertNotNull(resultado);
            assertEquals("Oleo Sintetico", resultado.nome());
            assertEquals(new BigDecimal("85.50"), resultado.preco());
            assertEquals(20, resultado.qtdEstoque());
            verify(pecaRepository).save(any(Peca.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando preco invalido")
        void shouldThrowExceptionForInvalidPrice() {
            var request = new CriarPecaRequest("Peca", "Desc", BigDecimal.ZERO, 10);
            assertThrows(ValidationException.class, () ->
                pecaService.cadastraPeca(request)
            );
        }
    }

    @Nested
    @DisplayName("Obter Peca")
    class ObterPecaTests {

        @Test
        @DisplayName("Deve obter peca por id")
        void shouldGetPartById() {
            when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));

            Optional<Peca> resultado = pecaService.getPecaById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("Oleo Sintetico", resultado.get().getNome());
        }

        @Test
        @DisplayName("Deve retornar vazio quando peca nao encontrada")
        void shouldReturnEmptyWhenPartNotFound() {
            when(pecaRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Peca> resultado = pecaService.getPecaById(999L);

            assertFalse(resultado.isPresent());
        }
    }

    @Nested
    @DisplayName("Atualizar Peca")
    class AtualizarPecaTests {

        @Test
        @DisplayName("Deve atualizar dados da peca")
        void shouldUpdatePart() {
            var request = new AtualizarPecaRequest("Oleo Premium", "10L", new BigDecimal("120.00"), 15);
            when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));
            when(pecaRepository.save(any(Peca.class))).thenReturn(peca);

            PecaDTO resultado = pecaService.atualizaPeca(1L, request);

            assertNotNull(resultado);
            verify(pecaRepository).save(any(Peca.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando peca nao encontrada")
        void shouldThrowExceptionWhenPartNotFound() {
            var request = new AtualizarPecaRequest("Oleo", "Desc", new BigDecimal("50"), 10);
            when(pecaRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                pecaService.atualizaPeca(999L, request)
            );
        }
    }

    @Nested
    @DisplayName("Deletar Peca")
    class DeletarPecaTests {

        @Test
        @DisplayName("Deve deletar peca com sucesso")
        void shouldDeletePart() {
            when(pecaRepository.findById(1L)).thenReturn(Optional.of(peca));
            pecaService.deletaPeca(1L);
            verify(pecaRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lancar excecao quando peca nao encontrada para exclusao")
        void shouldThrowExceptionWhenPartNotFoundForDelete() {
            when(pecaRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () ->
                pecaService.deletaPeca(999L)
            );
        }
    }
}
