package br.com.fiap.infrastructure.repository;

import br.com.fiap.domain.model.Peca;
import br.com.fiap.infrastructure.persistence.entity.PecaEntity;
import br.com.fiap.infrastructure.persistence.mapper.PecaMapper;
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
class PecaRepositoryImplTest {

    @Mock
    private PecaJpaRepository jpaRepository;

    private PecaRepositoryImpl repository;

    @BeforeEach
    void setup() {
        repository = new PecaRepositoryImpl(jpaRepository);
    }

    @Test
    void deveSalvarPeca() {

        Peca peca = mock(Peca.class);
        PecaEntity entity = mock(PecaEntity.class);
        PecaEntity savedEntity = mock(PecaEntity.class);
        Peca pecaSalva = mock(Peca.class);

        try (MockedStatic<PecaMapper> mapperMock =
                     mockStatic(PecaMapper.class)) {

            mapperMock.when(() -> PecaMapper.toEntity(peca))
                    .thenReturn(entity);

            when(jpaRepository.save(entity))
                    .thenReturn(savedEntity);

            mapperMock.when(() -> PecaMapper.toDomain(savedEntity))
                    .thenReturn(pecaSalva);

            Peca resultado = repository.save(peca);

            assertNotNull(resultado);
            assertEquals(pecaSalva, resultado);

            verify(jpaRepository).save(entity);
        }
    }

    @Test
    void deveBuscarPorIdQuandoExistir() {

        Long id = 1L;

        PecaEntity entity = mock(PecaEntity.class);
        Peca peca = mock(Peca.class);

        when(jpaRepository.findById(id))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<PecaMapper> mapperMock =
                     mockStatic(PecaMapper.class)) {

            mapperMock.when(() -> PecaMapper.toDomain(entity))
                    .thenReturn(peca);

            Optional<Peca> resultado = repository.findById(id);

            assertTrue(resultado.isPresent());
            assertEquals(peca, resultado.get());

            verify(jpaRepository).findById(id);
        }
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarPorId() {

        Long id = 1L;

        when(jpaRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Peca> resultado = repository.findById(id);

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