package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.Servico;
import br.com.fiap.infrastructure.persistence.entity.ServicoEntity;
import br.com.fiap.infrastructure.persistence.mapper.ServicoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoRepositoryImplTest {

    @Mock
    private ServicoJpaRepository jpaRepository;

    private ServicoRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new ServicoRepositoryImpl(jpaRepository);
    }

    @Test
    void deveSalvarServico() {

        Servico servico = mock(Servico.class);
        ServicoEntity entity = mock(ServicoEntity.class);
        ServicoEntity savedEntity = mock(ServicoEntity.class);
        Servico servicoSalvo = mock(Servico.class);

        try (MockedStatic<ServicoMapper> mapperMock =
                     mockStatic(ServicoMapper.class)) {

            mapperMock.when(() -> ServicoMapper.toEntity(servico))
                    .thenReturn(entity);

            when(jpaRepository.save(entity))
                    .thenReturn(savedEntity);

            mapperMock.when(() -> ServicoMapper.toDomain(savedEntity))
                    .thenReturn(servicoSalvo);

            Servico resultado = repository.save(servico);

            assertNotNull(resultado);
            assertEquals(servicoSalvo, resultado);

            verify(jpaRepository).save(entity);
        }
    }

    @Test
    void deveBuscarPorIdQuandoExistir() {

        Long id = 1L;

        ServicoEntity entity = mock(ServicoEntity.class);
        Servico servico = mock(Servico.class);

        when(jpaRepository.findById(id))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<ServicoMapper> mapperMock =
                     mockStatic(ServicoMapper.class)) {

            mapperMock.when(() -> ServicoMapper.toDomain(entity))
                    .thenReturn(servico);

            Optional<Servico> resultado = repository.findById(id);

            assertTrue(resultado.isPresent());
            assertEquals(servico, resultado.get());

            verify(jpaRepository).findById(id);
        }
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorId() {

        Long id = 1L;

        when(jpaRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Servico> resultado = repository.findById(id);

        assertTrue(resultado.isEmpty());

        verify(jpaRepository).findById(id);
    }

    @Test
    void deveDeletarPorId() {

        Long id = 1L;

        doNothing().when(jpaRepository).deleteById(id);

        repository.deleteById(id);

        verify(jpaRepository).deleteById(id);
    }
}