package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.Placa;
import br.com.fiap.domain.model.Veiculo;
import br.com.fiap.infrastructure.persistence.entity.ClienteEntity;
import br.com.fiap.infrastructure.persistence.entity.PlacaEntity;
import br.com.fiap.infrastructure.persistence.entity.VeiculoEntity;
import br.com.fiap.infrastructure.persistence.mapper.VeiculoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoRepositoryImplTest {

    @Mock
    private VeiculoJpaRepository jpaRepository;

    @Mock
    private ClienteJpaRepository clienteJpaRepository;

    private VeiculoRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new VeiculoRepositoryImpl(
                jpaRepository,
                clienteJpaRepository
        );
    }

    @Test
    void deveSalvarVeiculo() {

        Veiculo veiculo = mock(Veiculo.class);
        var cliente = mock(br.com.fiap.domain.model.Cliente.class);
        var placa = mock(Placa.class);

        when(cliente.getId()).thenReturn(1L);
        when(placa.getValue()).thenReturn("ABC1234");

        when(veiculo.getCliente()).thenReturn(cliente);
        when(veiculo.getPlaca()).thenReturn(placa);

        ClienteEntity clienteEntity = mock(ClienteEntity.class);
        VeiculoEntity veiculoEntitySalvo = mock(VeiculoEntity.class);
        Veiculo veiculoSalvo = mock(Veiculo.class);

        when(clienteJpaRepository.getReferenceById(1L))
                .thenReturn(clienteEntity);

        when(jpaRepository.save(any(VeiculoEntity.class)))
                .thenReturn(veiculoEntitySalvo);

        try (MockedStatic<VeiculoMapper> mapperMock =
                     mockStatic(VeiculoMapper.class)) {

            mapperMock.when(() -> VeiculoMapper.toDomain(veiculoEntitySalvo))
                    .thenReturn(veiculoSalvo);

            Veiculo resultado = repository.save(veiculo);

            assertNotNull(resultado);
            assertEquals(veiculoSalvo, resultado);

            verify(clienteJpaRepository).getReferenceById(1L);
            verify(jpaRepository).save(any(VeiculoEntity.class));
        }
    }

    @Test
    void deveBuscarPorIdQuandoExistir() {

        Long id = 1L;

        VeiculoEntity entity = mock(VeiculoEntity.class);
        Veiculo veiculo = mock(Veiculo.class);

        when(jpaRepository.findById(id))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<VeiculoMapper> mapperMock =
                     mockStatic(VeiculoMapper.class)) {

            mapperMock.when(() -> VeiculoMapper.toDomain(entity))
                    .thenReturn(veiculo);

            Optional<Veiculo> resultado = repository.findById(id);

            assertTrue(resultado.isPresent());
            assertEquals(veiculo, resultado.get());

            verify(jpaRepository).findById(id);
        }
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorId() {

        when(jpaRepository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<Veiculo> resultado = repository.findById(1L);

        assertTrue(resultado.isEmpty());

        verify(jpaRepository).findById(1L);
    }

    @Test
    void deveBuscarPorPlaca() {

        Placa placa = mock(Placa.class);
        when(placa.getValue()).thenReturn("ABC1234");

        VeiculoEntity entity = mock(VeiculoEntity.class);
        Veiculo veiculo = mock(Veiculo.class);

        when(jpaRepository.findByPlaca(any(PlacaEntity.class)))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<VeiculoMapper> mapperMock =
                     mockStatic(VeiculoMapper.class)) {

            mapperMock.when(() -> VeiculoMapper.toDomain(entity))
                    .thenReturn(veiculo);

            Optional<Veiculo> resultado = repository.findByPlaca(placa);

            assertTrue(resultado.isPresent());
            assertEquals(veiculo, resultado.get());

            verify(jpaRepository).findByPlaca(any(PlacaEntity.class));
        }
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorPlaca() {

        Placa placa = mock(Placa.class);
        when(placa.getValue()).thenReturn("ABC1234");

        when(jpaRepository.findByPlaca(any(PlacaEntity.class)))
                .thenReturn(Optional.empty());

        Optional<Veiculo> resultado = repository.findByPlaca(placa);

        assertTrue(resultado.isEmpty());

        verify(jpaRepository).findByPlaca(any(PlacaEntity.class));
    }

    @Test
    void deveBuscarVeiculosPorClienteId() {

        Long clienteId = 10L;

        VeiculoEntity entity1 = mock(VeiculoEntity.class);
        VeiculoEntity entity2 = mock(VeiculoEntity.class);

        Veiculo veiculo1 = mock(Veiculo.class);
        Veiculo veiculo2 = mock(Veiculo.class);

        when(jpaRepository.findByClienteId(clienteId))
                .thenReturn(List.of(entity1, entity2));

        try (MockedStatic<VeiculoMapper> mapperMock =
                     mockStatic(VeiculoMapper.class)) {

            mapperMock.when(() -> VeiculoMapper.toDomain(entity1))
                    .thenReturn(veiculo1);

            mapperMock.when(() -> VeiculoMapper.toDomain(entity2))
                    .thenReturn(veiculo2);

            List<Veiculo> resultado =
                    repository.findByClienteId(clienteId);

            assertEquals(2, resultado.size());
            assertTrue(resultado.contains(veiculo1));
            assertTrue(resultado.contains(veiculo2));
        }
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremVeiculosDoCliente() {

        when(jpaRepository.findByClienteId(10L))
                .thenReturn(List.of());

        List<Veiculo> resultado = repository.findByClienteId(10L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveDeletarPorId() {

        Long id = 1L;

        doNothing().when(jpaRepository).deleteById(id);

        repository.deleteById(id);

        verify(jpaRepository).deleteById(id);
    }
}