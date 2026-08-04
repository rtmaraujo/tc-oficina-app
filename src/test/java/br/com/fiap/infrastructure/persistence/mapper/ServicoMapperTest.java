package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.Servico;
import br.com.fiap.infrastructure.persistence.entity.ServicoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServicoMapper - Testes de Mapeamento de Serviço")
class ServicoMapperTest {

    @Nested
    @DisplayName("Conversão de Domain para Entity")
    class DomainToEntityTests {

        @Test
        @DisplayName("Deve converter Servico domain para ServicoEntity com sucesso")
        void shouldConvertServicoDomainToEntity() {
            var servico = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            var entity = ServicoMapper.toEntity(servico);

            assertNotNull(entity);
            assertEquals(1L, entity.getId());
            assertEquals("Troca de Óleo", entity.getNome());
            assertEquals("Troca de óleo do motor", entity.getDescricao());
            assertEquals(new BigDecimal("150.00"), entity.getPreco());
        }

        @Test
        @DisplayName("Deve converter Servico com valores diferentes")
        void shouldConvertServicoWithDifferentValues() {
            var servico = new Servico(2L, "Alinhamento", "Alinhamento de rodas", new BigDecimal("200.00"));

            var entity = ServicoMapper.toEntity(servico);

            assertNotNull(entity);
            assertEquals(2L, entity.getId());
            assertEquals("Alinhamento", entity.getNome());
            assertEquals("Alinhamento de rodas", entity.getDescricao());
            assertEquals(new BigDecimal("200.00"), entity.getPreco());
        }

        @Test
        @DisplayName("Deve retornar null ao converter Servico null")
        void shouldReturnNullWhenServicoIsNull() {
            var result = ServicoMapper.toEntity(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Conversão de Entity para Domain")
    class EntityToDomainTests {

        @Test
        @DisplayName("Deve converter ServicoEntity para Servico domain com sucesso")
        void shouldConvertServicoEntityToDomain() {
            var entity = new ServicoEntity(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            var servico = ServicoMapper.toDomain(entity);

            assertNotNull(servico);
            assertEquals(1L, servico.getId());
            assertEquals("Troca de Óleo", servico.getNome());
            assertEquals("Troca de óleo do motor", servico.getDescricao());
            assertEquals(new BigDecimal("150.00"), servico.getPreco());
        }

        @Test
        @DisplayName("Deve converter ServicoEntity com valores diferentes")
        void shouldConvertServicoEntityWithDifferentValues() {
            var entity = new ServicoEntity(2L, "Alinhamento", "Alinhamento de rodas", new BigDecimal("200.00"));

            var servico = ServicoMapper.toDomain(entity);

            assertNotNull(servico);
            assertEquals(2L, servico.getId());
            assertEquals("Alinhamento", servico.getNome());
            assertEquals("Alinhamento de rodas", servico.getDescricao());
            assertEquals(new BigDecimal("200.00"), servico.getPreco());
        }

        @Test
        @DisplayName("Deve retornar null ao converter ServicoEntity null")
        void shouldReturnNullWhenServicoEntityIsNull() {
            var result = ServicoMapper.toDomain(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Bidirecional Mapping Tests")
    class BidirectionalMappingTests {

        @Test
        @DisplayName("Deve mapear Servico -> Entity -> Servico com sucesso")
        void shouldMapServicoToEntityToDomain() {
            var servicoOriginal = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));

            var entity = ServicoMapper.toEntity(servicoOriginal);
            var servicoRecuperado = ServicoMapper.toDomain(entity);

            assertEquals(servicoOriginal.getId(), servicoRecuperado.getId());
            assertEquals(servicoOriginal.getNome(), servicoRecuperado.getNome());
            assertEquals(servicoOriginal.getDescricao(), servicoRecuperado.getDescricao());
            assertEquals(servicoOriginal.getPreco(), servicoRecuperado.getPreco());
        }
    }
}

