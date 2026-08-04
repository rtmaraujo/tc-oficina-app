package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.Peca;
import br.com.fiap.infrastructure.persistence.entity.PecaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PecaMapper - Testes de Mapeamento de Peça")
class PecaMapperTest {

    @Nested
    @DisplayName("Conversão de Domain para Entity")
    class DomainToEntityTests {

        @Test
        @DisplayName("Deve converter Peca domain para PecaEntity com sucesso")
        void shouldConvertPecaDomainToEntity() {
            var peca = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            var entity = PecaMapper.toEntity(peca);

            assertNotNull(entity);
            assertEquals(1L, entity.getId());
            assertEquals("Bateria", entity.getNome());
            assertEquals("Bateria 60Ah", entity.getDescricao());
            assertEquals(new BigDecimal("500.00"), entity.getPreco());
            assertEquals(10, entity.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve converter Peca com valores diferentes")
        void shouldConvertPecaWithDifferentValues() {
            var peca = new Peca(2L, "Óleo Motor", "Óleo Sintético 5W30", new BigDecimal("150.50"), 25);

            var entity = PecaMapper.toEntity(peca);

            assertNotNull(entity);
            assertEquals(2L, entity.getId());
            assertEquals("Óleo Motor", entity.getNome());
            assertEquals("Óleo Sintético 5W30", entity.getDescricao());
            assertEquals(new BigDecimal("150.50"), entity.getPreco());
            assertEquals(25, entity.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve retornar null ao converter Peca null")
        void shouldReturnNullWhenPecaIsNull() {
            var result = PecaMapper.toEntity(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Conversão de Entity para Domain")
    class EntityToDomainTests {

        @Test
        @DisplayName("Deve converter PecaEntity para Peca domain com sucesso")
        void shouldConvertPecaEntityToDomain() {
            var entity = new PecaEntity(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            var peca = PecaMapper.toDomain(entity);

            assertNotNull(peca);
            assertEquals(1L, peca.getId());
            assertEquals("Bateria", peca.getNome());
            assertEquals("Bateria 60Ah", peca.getDescricao());
            assertEquals(new BigDecimal("500.00"), peca.getPreco());
            assertEquals(10, peca.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve converter PecaEntity com valores diferentes")
        void shouldConvertPecaEntityWithDifferentValues() {
            var entity = new PecaEntity(2L, "Óleo Motor", "Óleo Sintético 5W30", new BigDecimal("150.50"), 25);

            var peca = PecaMapper.toDomain(entity);

            assertNotNull(peca);
            assertEquals(2L, peca.getId());
            assertEquals("Óleo Motor", peca.getNome());
            assertEquals("Óleo Sintético 5W30", peca.getDescricao());
            assertEquals(new BigDecimal("150.50"), peca.getPreco());
            assertEquals(25, peca.getQtdEstoque());
        }

        @Test
        @DisplayName("Deve retornar null ao converter PecaEntity null")
        void shouldReturnNullWhenPecaEntityIsNull() {
            var result = PecaMapper.toDomain(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Bidirecional Mapping Tests")
    class BidirectionalMappingTests {

        @Test
        @DisplayName("Deve mapear Peca -> Entity -> Peca com sucesso")
        void shouldMapPecaToEntityToDomain() {
            var pecaOriginal = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);

            var entity = PecaMapper.toEntity(pecaOriginal);
            var pecaRecuperada = PecaMapper.toDomain(entity);

            assertEquals(pecaOriginal.getId(), pecaRecuperada.getId());
            assertEquals(pecaOriginal.getNome(), pecaRecuperada.getNome());
            assertEquals(pecaOriginal.getDescricao(), pecaRecuperada.getDescricao());
            assertEquals(pecaOriginal.getPreco(), pecaRecuperada.getPreco());
            assertEquals(pecaOriginal.getQtdEstoque(), pecaRecuperada.getQtdEstoque());
        }
    }
}

