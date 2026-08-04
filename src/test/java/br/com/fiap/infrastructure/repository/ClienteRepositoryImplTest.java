package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import br.com.fiap.infrastructure.persistence.entity.ClienteEntity;
import br.com.fiap.infrastructure.persistence.entity.CpfCnpjEntity;
import br.com.fiap.infrastructure.persistence.mapper.ClienteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryImplTest {

    @Mock
    private ClienteJpaRepository jpaRepository;

    private ClienteRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new ClienteRepositoryImpl(jpaRepository);
    }

    @Test
    void deveSalvarCliente() {
        Cliente cliente = mock(Cliente.class);
        ClienteEntity entity = mock(ClienteEntity.class);
        ClienteEntity savedEntity = mock(ClienteEntity.class);
        Cliente clienteSalvo = mock(Cliente.class);

        try (MockedStatic<ClienteMapper> mapperMock = mockStatic(ClienteMapper.class)) {

            mapperMock.when(() -> ClienteMapper.toEntity(cliente))
                    .thenReturn(entity);

            when(jpaRepository.save(entity))
                    .thenReturn(savedEntity);

            mapperMock.when(() -> ClienteMapper.toDomain(savedEntity))
                    .thenReturn(clienteSalvo);

            Cliente resultado = repository.save(cliente);

            assertNotNull(resultado);
            assertEquals(clienteSalvo, resultado);

            verify(jpaRepository).save(entity);
        }
    }

    @Test
    void deveBuscarPorIdQuandoExistir() {
        Long id = 1L;

        ClienteEntity entity = mock(ClienteEntity.class);
        Cliente cliente = mock(Cliente.class);

        when(jpaRepository.findById(id))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<ClienteMapper> mapperMock = mockStatic(ClienteMapper.class)) {

            mapperMock.when(() -> ClienteMapper.toDomain(entity))
                    .thenReturn(cliente);

            Optional<Cliente> resultado = repository.findById(id);

            assertTrue(resultado.isPresent());
            assertEquals(cliente, resultado.get());

            verify(jpaRepository).findById(id);
        }
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorId() {
        Long id = 1L;

        when(jpaRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Cliente> resultado = repository.findById(id);

        assertTrue(resultado.isEmpty());

        verify(jpaRepository).findById(id);
    }

    @Test
    void deveBuscarPorCpfCnpj() {
        String numero = "12345678901";

        CpfCnpj cpfCnpj = mock(CpfCnpj.class);
        when(cpfCnpj.getValue()).thenReturn(numero);

        ClienteEntity entity = mock(ClienteEntity.class);
        Cliente cliente = mock(Cliente.class);

        when(jpaRepository.findByCpfCnpj(any(CpfCnpjEntity.class)))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<ClienteMapper> mapperMock = mockStatic(ClienteMapper.class)) {

            mapperMock.when(() -> ClienteMapper.toDomain(entity))
                    .thenReturn(cliente);

            Optional<Cliente> resultado = repository.findByCpfCnpj(cpfCnpj);

            assertTrue(resultado.isPresent());
            assertEquals(cliente, resultado.get());

            verify(jpaRepository).findByCpfCnpj(any(CpfCnpjEntity.class));
        }
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorCpfCnpj() {
        String numero = "12345678901";

        CpfCnpj cpfCnpj = mock(CpfCnpj.class);
        when(cpfCnpj.getValue()).thenReturn(numero);

        when(jpaRepository.findByCpfCnpj(any(CpfCnpjEntity.class)))
                .thenReturn(Optional.empty());

        Optional<Cliente> resultado = repository.findByCpfCnpj(cpfCnpj);

        assertTrue(resultado.isEmpty());

        verify(jpaRepository).findByCpfCnpj(any(CpfCnpjEntity.class));
    }

    @Test
    void deveDeletarPorId() {
        Long id = 1L;

        doNothing().when(jpaRepository).deleteById(id);

        repository.deleteById(id);

        verify(jpaRepository).deleteById(id);
    }
}