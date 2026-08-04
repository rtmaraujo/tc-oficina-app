package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.OrdemServico;
import br.com.fiap.infrastructure.persistence.entity.*;
import br.com.fiap.infrastructure.persistence.mapper.OrdemServicoMapper;
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
class OrdemServicoRepositoryImplTest {

    @Mock
    private OrdemServicoJpaRepository jpaRepository;

    @Mock
    private ClienteJpaRepository clienteJpaRepository;

    @Mock
    private VeiculoJpaRepository veiculoJpaRepository;

    @Mock
    private ServicoJpaRepository servicoJpaRepository;

    @Mock
    private PecaJpaRepository pecaJpaRepository;

    private OrdemServicoRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new OrdemServicoRepositoryImpl(
                jpaRepository,
                clienteJpaRepository,
                veiculoJpaRepository,
                servicoJpaRepository,
                pecaJpaRepository
        );
    }

    @Test
    void deveSalvarOrdemServico() {

        OrdemServico ordemServico = mock(OrdemServico.class);

        var cliente = mock(br.com.fiap.domain.model.Cliente.class);
        var veiculo = mock(br.com.fiap.domain.model.Veiculo.class);
        var servico = mock(br.com.fiap.domain.model.Servico.class);
        var peca = mock(br.com.fiap.domain.model.Peca.class);

        when(cliente.getId()).thenReturn(1L);
        when(veiculo.getId()).thenReturn(2L);
        when(servico.getId()).thenReturn(3L);
        when(peca.getId()).thenReturn(4L);

        when(ordemServico.getCliente()).thenReturn(cliente);
        when(ordemServico.getVeiculo()).thenReturn(veiculo);
        when(ordemServico.getServicos()).thenReturn(List.of(servico));
        when(ordemServico.getPecas()).thenReturn(List.of(peca));

        ClienteEntity clienteEntity = mock(ClienteEntity.class);
        VeiculoEntity veiculoEntity = mock(VeiculoEntity.class);
        ServicoEntity servicoEntity = mock(ServicoEntity.class);
        PecaEntity pecaEntity = mock(PecaEntity.class);

        when(clienteJpaRepository.getReferenceById(1L))
                .thenReturn(clienteEntity);

        when(veiculoJpaRepository.getReferenceById(2L))
                .thenReturn(veiculoEntity);

        when(servicoJpaRepository.getReferenceById(3L))
                .thenReturn(servicoEntity);

        when(pecaJpaRepository.getReferenceById(4L))
                .thenReturn(pecaEntity);

        OrdemServicoEntity savedEntity = mock(OrdemServicoEntity.class);
        OrdemServico ordemSalva = mock(OrdemServico.class);

        when(jpaRepository.save(any(OrdemServicoEntity.class)))
                .thenReturn(savedEntity);

        try (MockedStatic<OrdemServicoMapper> mapperMock =
                     mockStatic(OrdemServicoMapper.class)) {

            mapperMock.when(() -> OrdemServicoMapper.toDomain(savedEntity))
                    .thenReturn(ordemSalva);

            OrdemServico resultado = repository.save(ordemServico);

            assertNotNull(resultado);
            assertEquals(ordemSalva, resultado);

            verify(clienteJpaRepository).getReferenceById(1L);
            verify(veiculoJpaRepository).getReferenceById(2L);
            verify(servicoJpaRepository).getReferenceById(3L);
            verify(pecaJpaRepository).getReferenceById(4L);

            verify(jpaRepository).save(any(OrdemServicoEntity.class));
        }
    }

    @Test
    void deveBuscarPorId() {

        Long id = 1L;

        OrdemServicoEntity entity = mock(OrdemServicoEntity.class);
        OrdemServico ordemServico = mock(OrdemServico.class);

        when(jpaRepository.findById(id))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<OrdemServicoMapper> mapperMock =
                     mockStatic(OrdemServicoMapper.class)) {

            mapperMock.when(() -> OrdemServicoMapper.toDomain(entity))
                    .thenReturn(ordemServico);

            Optional<OrdemServico> resultado = repository.findById(id);

            assertTrue(resultado.isPresent());
            assertEquals(ordemServico, resultado.get());
        }
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorId() {

        when(jpaRepository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<OrdemServico> resultado = repository.findById(1L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarTodasOrdens() {

        OrdemServicoEntity entity1 = mock(OrdemServicoEntity.class);
        OrdemServicoEntity entity2 = mock(OrdemServicoEntity.class);

        OrdemServico ordem1 = mock(OrdemServico.class);
        OrdemServico ordem2 = mock(OrdemServico.class);

        when(jpaRepository.findAll())
                .thenReturn(List.of(entity1, entity2));

        try (MockedStatic<OrdemServicoMapper> mapperMock =
                     mockStatic(OrdemServicoMapper.class)) {

            mapperMock.when(() -> OrdemServicoMapper.toDomain(entity1))
                    .thenReturn(ordem1);

            mapperMock.when(() -> OrdemServicoMapper.toDomain(entity2))
                    .thenReturn(ordem2);

            List<OrdemServico> resultado = repository.findAll();

            assertEquals(2, resultado.size());
            assertTrue(resultado.contains(ordem1));
            assertTrue(resultado.contains(ordem2));
        }
    }

    @Test
    void deveBuscarPorClienteId() {

        Long clienteId = 10L;

        OrdemServicoEntity entity = mock(OrdemServicoEntity.class);
        OrdemServico ordem = mock(OrdemServico.class);

        when(jpaRepository.findByClienteId(clienteId))
                .thenReturn(List.of(entity));

        try (MockedStatic<OrdemServicoMapper> mapperMock =
                     mockStatic(OrdemServicoMapper.class)) {

            mapperMock.when(() -> OrdemServicoMapper.toDomain(entity))
                    .thenReturn(ordem);

            List<OrdemServico> resultado =
                    repository.findByClienteId(clienteId);

            assertEquals(1, resultado.size());
            assertEquals(ordem, resultado.getFirst());
        }
    }

    @Test
    void deveDeletarPorId() {

        Long id = 99L;

        doNothing().when(jpaRepository).deleteById(id);

        repository.deleteById(id);

        verify(jpaRepository).deleteById(id);
    }
}