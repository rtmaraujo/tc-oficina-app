package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.CpfCnpj;
import br.com.fiap.infrastructure.persistence.entity.ClienteEntity;
import br.com.fiap.infrastructure.persistence.entity.CpfCnpjEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClienteMapper - Testes de Mapeamento de Cliente")
class ClienteMapperTest {

    @Nested
    @DisplayName("Conversão de Domain para Entity")
    class DomainToEntityTests {

        @Test
        @DisplayName("Deve converter Cliente domain para ClienteEntity com sucesso")
        void shouldConvertClienteDomainToEntity() {
            var cpfCnpj = new CpfCnpj("12345678909");
            var cliente = new Cliente(1L, "John Doe", cpfCnpj, "john@example.com", "11987654321");

            var entity = ClienteMapper.toEntity(cliente);

            assertNotNull(entity);
            assertEquals(1L, entity.getId());
            assertEquals("John Doe", entity.getNome());
            assertEquals("john@example.com", entity.getEmail());
            assertEquals("11987654321", entity.getTelefone());
            assertNotNull(entity.getCpfCnpj());
            assertEquals("12345678909", entity.getCpfCnpj().getValue());
        }

        @Test
        @DisplayName("Deve converter Cliente com CNPJ para ClienteEntity")
        void shouldConvertClienteWithCnpjDomainToEntity() {
            var cpfCnpj = new CpfCnpj("11222333000181");
            var cliente = new Cliente(2L, "Tech Company", cpfCnpj, "contact@tech.com", "1133334444");

            var entity = ClienteMapper.toEntity(cliente);

            assertNotNull(entity);
            assertEquals(2L, entity.getId());
            assertEquals("Tech Company", entity.getNome());
            assertEquals("contact@tech.com", entity.getEmail());
            assertEquals("1133334444", entity.getTelefone());
            assertEquals("11222333000181", entity.getCpfCnpj().getValue());
        }

        @Test
        @DisplayName("Deve retornar null ao converter Cliente null")
        void shouldReturnNullWhenClienteIsNull() {
            var result = ClienteMapper.toEntity(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Deve converter Cliente com CpfCnpj null")
        void shouldConvertClienteWithNullCpfCnpj() {
            var cliente = new Cliente(3L, "Jane Doe", null, "jane@example.com", "11999999999");

            var entity = ClienteMapper.toEntity(cliente);

            assertNotNull(entity);
            assertEquals(3L, entity.getId());
            assertEquals("Jane Doe", entity.getNome());
            assertNull(entity.getCpfCnpj());
        }
    }

    @Nested
    @DisplayName("Conversão de Entity para Domain")
    class EntityToDomainTests {

        @Test
        @DisplayName("Deve converter ClienteEntity para Cliente domain com sucesso")
        void shouldConvertClienteEntityToDomain() {
            var cpfCnpjEntity = new CpfCnpjEntity("12345678909");
            var entity = new ClienteEntity(1L, "John Doe", cpfCnpjEntity, "john@example.com", "11987654321");

            var cliente = ClienteMapper.toDomain(entity);

            assertNotNull(cliente);
            assertEquals(1L, cliente.getId());
            assertEquals("John Doe", cliente.getNome());
            assertEquals("john@example.com", cliente.getEmail());
            assertEquals("11987654321", cliente.getTelefone());
            assertNotNull(cliente.getCpfCnpj());
            assertEquals("12345678909", cliente.getCpfCnpj().getValue());
        }

        @Test
        @DisplayName("Deve converter ClienteEntity com CNPJ para Cliente")
        void shouldConvertClienteEntityWithCnpjToDomain() {
            var cpfCnpjEntity = new CpfCnpjEntity("11222333000181");
            var entity = new ClienteEntity(2L, "Tech Company", cpfCnpjEntity, "contact@tech.com", "1133334444");

            var cliente = ClienteMapper.toDomain(entity);

            assertNotNull(cliente);
            assertEquals(2L, cliente.getId());
            assertEquals("Tech Company", cliente.getNome());
            assertEquals("contact@tech.com", cliente.getEmail());
            assertEquals("11222333000181", cliente.getCpfCnpj().getValue());
        }

        @Test
        @DisplayName("Deve retornar null ao converter ClienteEntity null")
        void shouldReturnNullWhenClienteEntityIsNull() {
            var result = ClienteMapper.toDomain(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Deve converter ClienteEntity com CpfCnpj null")
        void shouldConvertClienteEntityWithNullCpfCnpj() {
            var entity = new ClienteEntity(3L, "Jane Doe", null, "jane@example.com", "11999999999");

            var cliente = ClienteMapper.toDomain(entity);

            assertNotNull(cliente);
            assertEquals(3L, cliente.getId());
            assertEquals("Jane Doe", cliente.getNome());
            assertNull(cliente.getCpfCnpj());
        }
    }

    @Nested
    @DisplayName("Bidirecional Mapping Tests")
    class BidirectionalMappingTests {

        @Test
        @DisplayName("Deve mapear Cliente -> Entity -> Cliente com sucesso")
        void shouldMapClienteToEntityToDomain() {
            var cpfCnpj = new CpfCnpj("12345678909");
            var clienteOriginal = new Cliente(1L, "John Doe", cpfCnpj, "john@example.com", "11987654321");

            var entity = ClienteMapper.toEntity(clienteOriginal);
            var clienteRecuperado = ClienteMapper.toDomain(entity);

            assertEquals(clienteOriginal.getId(), clienteRecuperado.getId());
            assertEquals(clienteOriginal.getNome(), clienteRecuperado.getNome());
            assertEquals(clienteOriginal.getEmail(), clienteRecuperado.getEmail());
            assertEquals(clienteOriginal.getTelefone(), clienteRecuperado.getTelefone());
            assertEquals(clienteOriginal.getCpfCnpj().getValue(), clienteRecuperado.getCpfCnpj().getValue());
        }
    }
}