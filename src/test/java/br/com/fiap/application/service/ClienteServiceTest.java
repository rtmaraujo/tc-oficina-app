package br.com.fiap.application.service;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import br.com.fiap.domain.repository.ClienteRepository;
import br.com.fiap.presentation.dto.request.AtualizarClienteRequest;
import br.com.fiap.presentation.dto.request.CriarClienteRequest;
import br.com.fiap.presentation.dto.response.ClienteDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Testes de Aplicacao")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Nested
    @DisplayName("Criacao de Cliente")
    class CriarClienteTests {

        @Test
        @DisplayName("Deve criar e retornar cliente com CPF valido")
        void createClient_shouldCreateAndReturnClient() {
            var request = new CriarClienteRequest("John Doe", "12345678909", "john@example.com", "123456789");
            var cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "123456789");

            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            ClienteDTO result = clienteService.cadastraCliente(request);

            assertNotNull(result);
            assertEquals("John Doe", result.nome());
            verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve criar cliente com CNPJ valido")
        void createClient_shouldCreateAndReturnClientWithCNPJ() {
            var request = new CriarClienteRequest("Tech Company", "11222333000181", "contact@tech.com", "1199999999");
            var cliente = new Cliente("Tech Company", new CpfCnpj("11222333000181"), "contact@tech.com", "1199999999");

            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            ClienteDTO result = clienteService.cadastraCliente(request);

            assertNotNull(result);
            assertEquals("Tech Company", result.nome());
            verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve lancar excecao se CPF/CNPJ ja existe")
        void createClient_shouldThrowIfCpfCnpjExists() {
            var request = new CriarClienteRequest("John Doe", "12345678909", "john@example.com", "123456789");
            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.of(mock(Cliente.class)));

            assertThrows(ValidationException.class, () ->
                clienteService.cadastraCliente(request)
            );

            verify(clienteRepository, never()).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve salvar cliente quando CPF/CNPJ nao existe")
        void createClient_shouldSaveWhenCpfCnpjDoesNotExist() {
            var request = new CriarClienteRequest("Jane Doe", "12345678909", "jane@example.com", "987654321");
            var cliente = new Cliente("Jane Doe", new CpfCnpj("12345678909"), "jane@example.com", "987654321");

            when(clienteRepository.findByCpfCnpj(any())).thenReturn(Optional.empty());
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            ClienteDTO result = clienteService.cadastraCliente(request);

            verify(clienteRepository).findByCpfCnpj(any());
            verify(clienteRepository).save(any(Cliente.class));
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("Obter Cliente")
    class GetClienteTests {

        @Test
        @DisplayName("Deve obter cliente por ID")
        void shouldGetClientById() {
            var cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "123456789");
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

            var result = clienteService.getClienteById(1L);

            assertTrue(result.isPresent());
            assertEquals("John Doe", result.get().getNome());
            verify(clienteRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar vazio quando cliente nao encontrado")
        void shouldReturnEmptyWhenClientNotFound() {
            when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

            var result = clienteService.getClienteById(999L);

            assertFalse(result.isPresent());
            verify(clienteRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("Atualizar Cliente")
    class UpdateClienteTests {

        @Test
        @DisplayName("Deve atualizar cliente com dados validos")
        void shouldUpdateClient() {
            var request = new AtualizarClienteRequest("John Updated", "john.updated@example.com", "987654321");
            var cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "123456789");
            var clienteAtualizado = new Cliente("John Updated", new CpfCnpj("12345678909"), "john.updated@example.com", "987654321");

            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

            ClienteDTO result = clienteService.atualizaCliente(1L, request);

            assertNotNull(result);
            assertEquals("John Updated", result.nome());
            assertEquals("john.updated@example.com", result.email());
            verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve lancar excecao ao atualizar cliente inexistente")
        void shouldThrowExceptionWhenClientNotFound() {
            var request = new AtualizarClienteRequest("John", "john@example.com", "123456789");
            when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                clienteService.atualizaCliente(999L, request)
            );

            verify(clienteRepository, never()).save(any(Cliente.class));
        }
    }

    @Nested
    @DisplayName("Deletar Cliente")
    class DeleteClienteTests {

        @Test
        @DisplayName("Deve deletar cliente com sucesso")
        void shouldDeleteClient() {
            var cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "123456789");
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            clienteService.deletaCliente(1L);
            verify(clienteRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lancar excecao ao deletar cliente inexistente")
        void shouldThrowExceptionWhenClientNotFoundForDelete() {
            when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () ->
                clienteService.deletaCliente(999L)
            );
        }
    }
}
