package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.*;
import br.com.fiap.infrastructure.persistence.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VeiculoMapper - Testes de Mapeamento de Veículo")
class VeiculoMapperTest {

    @Nested
    @DisplayName("Conversão de Domain para Entity")
    class DomainToEntityTests {

        @Test
        @DisplayName("Deve converter Veiculo domain para VeiculoEntity com sucesso")
        void shouldConvertVeiculoDomainToEntity() {
            var placa = new Placa("ABC1234");
            var cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);

            var entity = VeiculoMapper.toEntity(veiculo);

            assertNotNull(entity);
            assertEquals(1L, entity.getId());
            assertEquals("ABC1234", entity.getPlaca().getValue());
            assertEquals("Toyota", entity.getMarca());
            assertEquals("Corolla", entity.getModelo());
            assertEquals(2020, entity.getAno());
            assertNotNull(entity.getCliente());
            assertEquals("John Doe", entity.getCliente().getNome());
        }

        @Test
        @DisplayName("Deve converter Veiculo com cliente null")
        void shouldConvertVeiculoWithNullCliente() {
            var placa = new Placa("XYZ9876");
            var veiculo = new Veiculo(2L, placa, "Honda", "Civic", 2021, null);

            var entity = VeiculoMapper.toEntity(veiculo);

            assertNotNull(entity);
            assertEquals(2L, entity.getId());
            assertEquals("XYZ9876", entity.getPlaca().getValue());
            assertEquals("Honda", entity.getMarca());
            assertNull(entity.getCliente());
        }

        @Test
        @DisplayName("Deve converter Veiculo com placa null")
        void shouldConvertVeiculoWithNullPlaca() {
            var cliente = new Cliente("Jane Doe", new CpfCnpj("12345678909"), "jane@example.com", "11987654321");
            var veiculo = new Veiculo(3L, null, "Ford", "Focus", 2022, cliente);

            var entity = VeiculoMapper.toEntity(veiculo);

            assertNotNull(entity);
            assertEquals(3L, entity.getId());
            assertNull(entity.getPlaca());
            assertEquals("Ford", entity.getMarca());
        }

        @Test
        @DisplayName("Deve retornar null ao converter Veiculo null")
        void shouldReturnNullWhenVeiculoIsNull() {
            var result = VeiculoMapper.toEntity(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Conversão de Entity para Domain")
    class EntityToDomainTests {

        @Test
        @DisplayName("Deve converter VeiculoEntity para Veiculo domain com sucesso")
        void shouldConvertVeiculoEntityToDomain() {
            var placaEntity = new PlacaEntity("ABC1234");
            var clienteEntity = new ClienteEntity(1L, "John Doe", new CpfCnpjEntity("12345678909"), "john@example.com", "11987654321");
            var entity = new VeiculoEntity(1L, placaEntity, "Toyota", "Corolla", 2020, clienteEntity);

            var veiculo = VeiculoMapper.toDomain(entity);

            assertNotNull(veiculo);
            assertEquals(1L, veiculo.getId());
            assertEquals("ABC1234", veiculo.getPlaca().getValue());
            assertEquals("Toyota", veiculo.getMarca());
            assertEquals("Corolla", veiculo.getModelo());
            assertEquals(2020, veiculo.getAno());
            assertNotNull(veiculo.getCliente());
            assertEquals("John Doe", veiculo.getCliente().getNome());
        }

        @Test
        @DisplayName("Deve converter VeiculoEntity com cliente null")
        void shouldConvertVeiculoEntityWithNullCliente() {
            var placaEntity = new PlacaEntity("XYZ9876");
            var entity = new VeiculoEntity(2L, placaEntity, "Honda", "Civic", 2021, null);

            var veiculo = VeiculoMapper.toDomain(entity);

            assertNotNull(veiculo);
            assertEquals(2L, veiculo.getId());
            assertEquals("XYZ9876", veiculo.getPlaca().getValue());
            assertEquals("Honda", veiculo.getMarca());
            assertNull(veiculo.getCliente());
        }

        @Test
        @DisplayName("Deve converter VeiculoEntity com placa null")
        void shouldConvertVeiculoEntityWithNullPlaca() {
            var clienteEntity = new ClienteEntity(1L, "Jane Doe", new CpfCnpjEntity("12345678909"), "jane@example.com", "11987654321");
            var entity = new VeiculoEntity(3L, null, "Ford", "Focus", 2022, clienteEntity);

            var veiculo = VeiculoMapper.toDomain(entity);

            assertNotNull(veiculo);
            assertEquals(3L, veiculo.getId());
            assertNull(veiculo.getPlaca());
            assertEquals("Ford", veiculo.getMarca());
        }

        @Test
        @DisplayName("Deve retornar null ao converter VeiculoEntity null")
        void shouldReturnNullWhenVeiculoEntityIsNull() {
            var result = VeiculoMapper.toDomain(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Bidirecional Mapping Tests")
    class BidirectionalMappingTests {

        @Test
        @DisplayName("Deve mapear Veiculo -> Entity -> Veiculo com sucesso")
        void shouldMapVeiculoToEntityToDomain() {
            var placa = new Placa("ABC1234");
            var cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
            var veiculoOriginal = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);

            var entity = VeiculoMapper.toEntity(veiculoOriginal);
            var veiculoRecuperado = VeiculoMapper.toDomain(entity);

            assertEquals(veiculoOriginal.getId(), veiculoRecuperado.getId());
            assertEquals(veiculoOriginal.getPlaca().getValue(), veiculoRecuperado.getPlaca().getValue());
            assertEquals(veiculoOriginal.getMarca(), veiculoRecuperado.getMarca());
            assertEquals(veiculoOriginal.getModelo(), veiculoRecuperado.getModelo());
            assertEquals(veiculoOriginal.getAno(), veiculoRecuperado.getAno());
        }
    }
}

