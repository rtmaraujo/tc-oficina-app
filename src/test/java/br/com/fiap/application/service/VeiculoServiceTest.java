package br.com.fiap.application.service;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import br.com.fiap.domain.model.Placa;
import br.com.fiap.domain.model.Veiculo;
import br.com.fiap.domain.repository.ClienteRepository;
import br.com.fiap.domain.repository.VeiculoRepository;
import br.com.fiap.presentation.dto.request.AtualizarVeiculoRequest;
import br.com.fiap.presentation.dto.request.CriarVeiculoRequest;
import br.com.fiap.presentation.dto.response.VeiculoDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VeiculoService - Testes de Aplicacao")
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VeiculoService veiculoService;

    private Cliente cliente;
    private Placa placa;
    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        CpfCnpj cpf = new CpfCnpj("12345678909");
        cliente = new Cliente("Joao Silva", cpf, "joao@example.com", "11987654321");
        placa = new Placa("ABC1234");
        veiculo = new Veiculo(placa, "Toyota", "Corolla", 2015, cliente);
        veiculo = spy(veiculo);
    }

    @Nested
    @DisplayName("Criar Veiculo")
    class CriarVeiculoTests {

        @Test
        @DisplayName("Deve criar veiculo com dados validos")
        void shouldCreateVehicle() {
            var request = new CriarVeiculoRequest("ABC1234", "Toyota", "Corolla", 2015, 1L);
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(veiculoRepository.findByPlaca(any())).thenReturn(Optional.empty());
            when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

            VeiculoDTO resultado = veiculoService.cadastraVeiculo(request);

            assertNotNull(resultado);
            assertEquals("Toyota", resultado.marca());
            assertEquals("Corolla", resultado.modelo());
            assertEquals(2015, resultado.ano());
            verify(veiculoRepository).save(any(Veiculo.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando cliente nao encontrado")
        void shouldThrowExceptionWhenClientNotFound() {
            var request = new CriarVeiculoRequest("ABC1234", "Toyota", "Corolla", 2015, 999L);
            when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                veiculoService.cadastraVeiculo(request)
            );
        }

        @Test
        @DisplayName("Deve lancar excecao quando placa ja existe")
        void shouldThrowExceptionWhenPlacaExists() {
            var request = new CriarVeiculoRequest("ABC1234", "Toyota", "Corolla", 2015, 1L);
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(veiculoRepository.findByPlaca(any())).thenReturn(Optional.of(veiculo));

            assertThrows(ValidationException.class, () ->
                veiculoService.cadastraVeiculo(request)
            );
        }
    }

    @Nested
    @DisplayName("Obter Veiculo")
    class ObterVeiculoTests {

        @Test
        @DisplayName("Deve obter veiculo por id")
        void shouldGetVehicleById() {
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

            Optional<Veiculo> resultado = veiculoService.getVeiculoById(1L);

            assertTrue(resultado.isPresent());
            assertEquals("Toyota", resultado.get().getMarca());
        }

        @Test
        @DisplayName("Deve retornar vazio quando veiculo nao encontrado")
        void shouldReturnEmptyWhenVehicleNotFound() {
            when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Veiculo> resultado = veiculoService.getVeiculoById(999L);

            assertFalse(resultado.isPresent());
        }

        @Test
        @DisplayName("Deve obter veiculos por cliente")
        void shouldGetVehiclesByClientId() {
            List<Veiculo> veiculos = new ArrayList<>();
            veiculos.add(veiculo);

            when(veiculoRepository.findByClienteId(1L)).thenReturn(veiculos);

            List<VeiculoDTO> resultado = veiculoService.getVeiculosByClienteId(1L);

            assertEquals(1, resultado.size());
            assertEquals("Toyota", resultado.getFirst().marca());
        }
    }

    @Nested
    @DisplayName("Atualizar Veiculo")
    class AtualizarVeiculoTests {

        @Test
        @DisplayName("Deve atualizar dados do veiculo")
        void shouldUpdateVehicle() {
            var request = new AtualizarVeiculoRequest("Honda", "Civic", 2020);
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

            VeiculoDTO resultado = veiculoService.atualizaVeiculo(1L, request);

            assertNotNull(resultado);
            verify(veiculoRepository).save(any(Veiculo.class));
        }

        @Test
        @DisplayName("Deve lancar excecao quando veiculo nao encontrado")
        void shouldThrowExceptionWhenVehicleNotFound() {
            var request = new AtualizarVeiculoRequest("Honda", "Civic", 2020);
            when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                veiculoService.atualizaVeiculo(999L, request)
            );
        }
    }

    @Nested
    @DisplayName("Deletar Veiculo")
    class DeletarVeiculoTests {

        @Test
        @DisplayName("Deve deletar veiculo")
        void shouldDeleteVehicle() {
            when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
            veiculoService.deletaVeiculo(1L);
            verify(veiculoRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lancar excecao quando veiculo nao encontrado para exclusao")
        void shouldThrowExceptionWhenVehicleNotFoundForDelete() {
            when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () ->
                veiculoService.deletaVeiculo(999L)
            );
        }
    }
}
