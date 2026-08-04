package br.com.fiap.infrastructure.persistence.mapper;

import br.com.fiap.domain.model.*;
import br.com.fiap.infrastructure.persistence.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrdemServicoMapper - Testes de Mapeamento de Ordem de Serviço")
class OrdemServicoMapperTest {

    @Nested
    @DisplayName("Conversão de Domain para Entity")
    class DomainToEntityTests {

        @Test
        @DisplayName("Deve converter OrdemServico domain para OrdemServicoEntity com sucesso")
        void shouldConvertOrdemServicoDomainToEntity() {
            var cliente = new Cliente(1L, "John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
            var placa = new Placa("ABC1234");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);
            var servico = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            var peca = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);
            var data = LocalDateTime.now();

            var ordem = new OrdemServico(1L, cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca), data);

            var entity = OrdemServicoMapper.toEntity(ordem);

            assertNotNull(entity);
            assertEquals(1L, entity.getId());
            assertNotNull(entity.getCliente());
            assertEquals("John Doe", entity.getCliente().getNome());
            assertNotNull(entity.getVeiculo());
            assertEquals("ABC1234", entity.getVeiculo().getPlaca().getValue());
            assertEquals(1, entity.getServicos().size());
            assertEquals(1, entity.getPecas().size());
        }

        @Test
        @DisplayName("Deve converter OrdemServico com listas vazias")
        void shouldConvertOrdemServicoWithEmptyLists() {
            var cliente = new Cliente(1L, "John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
            var placa = new Placa("ABC1234");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);
            var data = LocalDateTime.now();

            var ordem = new OrdemServico(1L, cliente, veiculo, Collections.emptyList(), Collections.emptyList(), data);

            var entity = OrdemServicoMapper.toEntity(ordem);

            assertNotNull(entity);
            assertEquals(1L, entity.getId());
            assertNotNull(entity.getServicos());
            assertEquals(0, entity.getServicos().size());
            assertNotNull(entity.getPecas());
            assertEquals(0, entity.getPecas().size());
        }

        @Test
        @DisplayName("Deve retornar null ao converter OrdemServico null")
        void shouldReturnNullWhenOrdemServicoIsNull() {
            var result = OrdemServicoMapper.toEntity(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Deve converter OrdemServico com cliente null")
        void shouldConvertOrdemServicoWithNullCliente() {
            var placa = new Placa("ABC1234");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, null);
            var data = LocalDateTime.now();

            var ordem = new OrdemServico(1L, null, veiculo, Collections.emptyList(), Collections.emptyList(), data);

            var entity = OrdemServicoMapper.toEntity(ordem);

            assertNotNull(entity);
            assertNull(entity.getCliente());
        }
    }

    @Nested
    @DisplayName("Conversão de Entity para Domain")
    class EntityToDomainTests {

        @Test
        @DisplayName("Deve converter OrdemServicoEntity para OrdemServico domain com sucesso")
        void shouldConvertOrdemServicoEntityToDomain() {
            var cpfCnpjEntity = new CpfCnpjEntity("12345678909");
            var clienteEntity = new ClienteEntity(1L, "John Doe", cpfCnpjEntity, "john@example.com", "11987654321");
            var placaEntity = new PlacaEntity("ABC1234");
            var veiculoEntity = new VeiculoEntity(1L, placaEntity, "Toyota", "Corolla", 2020, clienteEntity);
            var servicoEntity = new ServicoEntity(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            var pecaEntity = new PecaEntity(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);
            var data = LocalDateTime.now();

            var entity = new OrdemServicoEntity(1L, clienteEntity, veiculoEntity, OrdemServicoStatus.RECEBIDA, data, null, Arrays.asList(servicoEntity), Arrays.asList(pecaEntity), new BigDecimal("650.00"));

            var ordem = OrdemServicoMapper.toDomain(entity);

            assertNotNull(ordem);
            assertEquals(1L, ordem.getId());
            assertNotNull(ordem.getCliente());
            assertEquals("John Doe", ordem.getCliente().getNome());
            assertNotNull(ordem.getVeiculo());
            assertEquals("ABC1234", ordem.getVeiculo().getPlaca().getValue());
            assertEquals(1, ordem.getServicos().size());
            assertEquals(1, ordem.getPecas().size());
        }

        @Test
        @DisplayName("Deve converter OrdemServicoEntity com listas vazias")
        void shouldConvertOrdemServicoEntityWithEmptyLists() {
            var cpfCnpjEntity = new CpfCnpjEntity("12345678909");
            var clienteEntity = new ClienteEntity(1L, "John Doe", cpfCnpjEntity, "john@example.com", "11987654321");
            var placaEntity = new PlacaEntity("ABC1234");
            var veiculoEntity = new VeiculoEntity(1L, placaEntity, "Toyota", "Corolla", 2020, clienteEntity);
            var data = LocalDateTime.now();

            var entity = new OrdemServicoEntity(1L, clienteEntity, veiculoEntity, OrdemServicoStatus.RECEBIDA, data, null, Collections.emptyList(), Collections.emptyList(), BigDecimal.ZERO);

            var ordem = OrdemServicoMapper.toDomain(entity);

            assertNotNull(ordem);
            assertEquals(1L, ordem.getId());
            assertNotNull(ordem.getServicos());
            assertEquals(0, ordem.getServicos().size());
            assertNotNull(ordem.getPecas());
            assertEquals(0, ordem.getPecas().size());
        }

        @Test
        @DisplayName("Deve retornar null ao converter OrdemServicoEntity null")
        void shouldReturnNullWhenOrdemServicoEntityIsNull() {
            var result = OrdemServicoMapper.toDomain(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Deve converter OrdemServicoEntity com finalizadoEm preenchido")
        void shouldConvertOrdemServicoEntityWithFinalizadoEm() {
            var cpfCnpjEntity = new CpfCnpjEntity("12345678909");
            var clienteEntity = new ClienteEntity(1L, "John Doe", cpfCnpjEntity, "john@example.com", "11987654321");
            var placaEntity = new PlacaEntity("ABC1234");
            var veiculoEntity = new VeiculoEntity(1L, placaEntity, "Toyota", "Corolla", 2020, clienteEntity);
            var dataCriacao = LocalDateTime.now();
            var dataFinalizacao = dataCriacao.plusDays(1);

            var entity = new OrdemServicoEntity(1L, clienteEntity, veiculoEntity, OrdemServicoStatus.ENTREGUE, dataCriacao, dataFinalizacao, Collections.emptyList(), Collections.emptyList(), BigDecimal.ZERO);

            var ordem = OrdemServicoMapper.toDomain(entity);

            assertNotNull(ordem);
            assertEquals(dataFinalizacao, ordem.getFinalizadoEm());
        }
    }

    @Nested
    @DisplayName("Bidirecional Mapping Tests")
    class BidirectionalMappingTests {

        @Test
        @DisplayName("Deve mapear OrdemServico -> Entity -> OrdemServico com sucesso")
        void shouldMapOrdemServicoToEntityToDomain() {
            var cliente = new Cliente(1L, "John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");
            var placa = new Placa("ABC1234");
            var veiculo = new Veiculo(1L, placa, "Toyota", "Corolla", 2020, cliente);
            var servico = new Servico(1L, "Troca de Óleo", "Troca de óleo do motor", new BigDecimal("150.00"));
            var peca = new Peca(1L, "Bateria", "Bateria 60Ah", new BigDecimal("500.00"), 10);
            var data = LocalDateTime.now();

            var ordemOriginal = new OrdemServico(1L, cliente, veiculo, Arrays.asList(servico), Arrays.asList(peca), data);

            var entity = OrdemServicoMapper.toEntity(ordemOriginal);
            var ordemRecuperada = OrdemServicoMapper.toDomain(entity);

            assertEquals(ordemOriginal.getId(), ordemRecuperada.getId());
            assertEquals(ordemOriginal.getCliente().getNome(), ordemRecuperada.getCliente().getNome());
            assertEquals(ordemOriginal.getVeiculo().getPlaca().getValue(), ordemRecuperada.getVeiculo().getPlaca().getValue());
            assertEquals(ordemOriginal.getServicos().size(), ordemRecuperada.getServicos().size());
            assertEquals(ordemOriginal.getPecas().size(), ordemRecuperada.getPecas().size());
        }
    }
}

