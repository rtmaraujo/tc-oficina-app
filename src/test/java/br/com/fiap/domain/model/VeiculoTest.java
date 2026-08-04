package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Vehicle - Testes de Entidade de Domínio")
class VeiculoTest {

    @Nested
    @DisplayName("Criação e Validação de Vehicle")
    class CreationTests {

        private Cliente createTestClient() {
            return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
        }

        @Test
        @DisplayName("Deve criar veículo com dados válidos")
        void shouldCreateVehicleWithValidData() {
            Placa plate = new Placa("ABC-1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2023, cliente);

            assertNotNull(veiculo);
            assertEquals(plate, veiculo.getPlaca());
            assertEquals("Toyota", veiculo.getMarca());
            assertEquals("Corolla", veiculo.getModelo());
            assertEquals(2023, veiculo.getAno());
            assertEquals(cliente, veiculo.getCliente());
        }

        @Test
        @DisplayName("Deve criar veículo com placa formato Mercosul")
        void shouldCreateVehicleWithMercosulPlate() {
            Placa plate = new Placa("ABC1D23");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Honda", "Civic", 2024, cliente);

            assertNotNull(veiculo);
            assertEquals("ABC1D23", veiculo.getPlaca().getValue());
            assertEquals("Honda", veiculo.getMarca());
        }
    }

    @Nested
    @DisplayName("Igualdade de Veículos")
    class EqualityTests {

        private Cliente createTestClient() {
            return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
        }

        @Test
        @DisplayName("Deve armazenar cliente corretamente")
        void shouldStoreClientCorrectly() {
            Placa plate = new Placa("ABC-1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2023, cliente);
            
            assertEquals(cliente, veiculo.getCliente());
        }

        @Test
        @DisplayName("Deve ser diferente de null")
        void shouldNotEqualNull() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2023, cliente);
            assertNotEquals(veiculo, null);
        }

        @Test
        @DisplayName("Deve ser diferente de objeto de outro tipo")
        void shouldNotEqualDifferentType() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2023, cliente);
            assertNotEquals(veiculo, "ABC1234");
        }

        @Test
        @DisplayName("Deve ter o mesmo hashCode para veículos com mesmo ID")
        void shouldHaveSameHashCodeForEqualVeiculos() {
            Placa plate1 = new Placa("ABC1234");
            Placa plate2 = new Placa("XYZ9876");
            Cliente cliente = createTestClient();
            Veiculo veiculo1 = new Veiculo(1L, plate1, "Toyota", "Corolla", 2020, cliente);
            Veiculo veiculo2 = new Veiculo(1L, plate2, "Honda", "Civic", 2021, cliente);

            assertEquals(veiculo1.hashCode(), veiculo2.hashCode());
        }
    }

    @Nested
    @DisplayName("Formatação de Placa no Vehicle")
    class PlateFormatTests {

        private Cliente createTestClient() {
            return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
        }

        @Test
        @DisplayName("Deve normalizar placa com formatação variada")
        void shouldNormalizeVaryingPlateFormats() {
            Placa plate = new Placa("A-B-C-1-2-3-4");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2023, cliente);
            
            assertEquals("ABC1234", veiculo.getPlaca().getValue());
        }

        @Test
        @DisplayName("Deve normalizar placa com letras minúsculas")
        void shouldNormalizeLowercasePlate() {
            Placa plate = new Placa("abc1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2023, cliente);
            
            assertEquals("ABC1234", veiculo.getPlaca().getValue());
        }
    }

    @Nested
    @DisplayName("Classificação de Idade do Veículo")
    class IdadeVeiculoTests {

        private Cliente createTestClient() {
            return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
        }

        @Test
        @DisplayName("Deve identificar veículo antigo (antes de 2010)")
        void shouldIdentifyOldVeiculo() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2008, cliente);

            assertTrue(veiculo.isVeiculoAntigo());
            assertFalse(veiculo.isVeiculoNovo());
        }

        @Test
        @DisplayName("Deve identificar veículo novo (2020 ou depois)")
        void shouldIdentifyNewVeiculo() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            assertTrue(veiculo.isVeiculoNovo());
            assertFalse(veiculo.isVeiculoAntigo());
        }

        @Test
        @DisplayName("Deve identificar veículo intermediário (2010-2019)")
        void shouldIdentifyIntermediateVeiculo() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2015, cliente);

            assertFalse(veiculo.isVeiculoAntigo());
            assertFalse(veiculo.isVeiculoNovo());
        }

        @Test
        @DisplayName("Deve calcular idade do veículo corretamente")
        void shouldCalculateVeiculoAgeCorrectly() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            int anoAtual = Year.now().getValue();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2015, cliente);

            assertEquals(anoAtual - 2015, veiculo.getIdadeVeiculo());
        }
    }

    @Nested
    @DisplayName("Descrição e Informações")
    class DescricaoTests {

        private Cliente createTestClient() {
            return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
        }

        @Test
        @DisplayName("Deve gerar descrição geral corretamente")
        void shouldGenerateDescricaoGeralCorrectly() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            assertEquals("2020 Toyota Corolla", veiculo.getDescricaoGeral());
        }

        @Test
        @DisplayName("Deve gerar descrição para veículo antigo")
        void shouldGenerateDescricaoForOldVeiculo() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Volkswagen", "Fusca", 1980, cliente);

            assertEquals("1980 Volkswagen Fusca", veiculo.getDescricaoGeral());
        }
    }

    @Nested
    @DisplayName("Atualização de Informações")
    class AtualizacaoInfoTests {

        private Cliente createTestClient() {
            return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
        }

        @Test
        @DisplayName("Deve atualizar informações do veículo com sucesso")
        void shouldUpdateVeiculoInfoSuccessfully() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            veiculo.atualizaVeiculoInfo("Honda", "Civic", 2021);

            assertEquals("Honda", veiculo.getMarca());
            assertEquals("Civic", veiculo.getModelo());
            assertEquals(2021, veiculo.getAno());
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com marca nula")
        void shouldThrowWhenUpdatingWithNullMarca() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            assertThrows(IllegalArgumentException.class, () ->
                veiculo.atualizaVeiculoInfo(null, "Civic", 2021)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com modelo nulo")
        void shouldThrowWhenUpdatingWithNullModelo() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            assertThrows(IllegalArgumentException.class, () ->
                veiculo.atualizaVeiculoInfo("Honda", null, 2021)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com ano inválido (antes de 1900)")
        void shouldThrowWhenUpdatingWithInvalidYearBefore1900() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            assertThrows(IllegalArgumentException.class, () ->
                veiculo.atualizaVeiculoInfo("Honda", "Civic", 1899)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com ano inválido (depois de 2030)")
        void shouldThrowWhenUpdatingWithInvalidYearAfter2030() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            assertThrows(IllegalArgumentException.class, () ->
                veiculo.atualizaVeiculoInfo("Honda", "Civic", 2031)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar com ano nulo")
        void shouldThrowWhenUpdatingWithNullAno() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            assertThrows(IllegalArgumentException.class, () ->
                veiculo.atualizaVeiculoInfo("Honda", "Civic", null)
            );
        }

        @Test
        @DisplayName("Deve aceitar ano válido em limite inferior (1900)")
        void shouldAcceptValidYearAt1900() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            veiculo.atualizaVeiculoInfo("Oldsmobile", "Model", 1900);

            assertEquals(1900, veiculo.getAno());
        }

        @Test
        @DisplayName("Deve aceitar ano válido em limite superior (2030)")
        void shouldAcceptValidYearAt2030() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            veiculo.atualizaVeiculoInfo("Toyota", "Future", 2030);

            assertEquals(2030, veiculo.getAno());
        }
    }

    @Nested
    @DisplayName("Relacionamento com Cliente")
    class ClienteRelationshipTests {

        private Cliente createTestClient() {
            return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
        }

        @Test
        @DisplayName("Deve verificar se veículo pertence ao cliente")
        void shouldVerifyIfVeiculoBelongsToCliente() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            assertTrue(veiculo.belongsTo(cliente));
        }

        @Test
        @DisplayName("Deve retornar false se veículo não pertence ao cliente")
        void shouldReturnFalseIfVeiculoDoesNotBelongToCliente() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente1 = new Cliente(1L, "Client 1", new CpfCnpj("12345678909"), "client1@example.com", "11987654321");
            Cliente cliente2 = new Cliente(2L, "Client 2", new CpfCnpj("98765432100"), "client2@example.com", "11987654321");
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente1);

            assertFalse(veiculo.belongsTo(cliente2));
        }

        @Test
        @DisplayName("Deve retornar false se veículo não possui cliente")
        void shouldReturnFalseIfVeiculoHasNoCliente() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, null);

            assertFalse(veiculo.belongsTo(cliente));
        }

        @Test
        @DisplayName("Deve definir e obter cliente")
        void shouldSetAndGetCliente() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente1 = new Cliente(1L, "Client 1", new CpfCnpj("12345678909"), "client1@example.com", "11987654321");
            Cliente cliente2 = new Cliente(2L, "Client 2", new CpfCnpj("98765432100"), "client2@example.com", "11987654321");
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente1);

            veiculo.setCliente(cliente2);

            assertEquals(cliente2, veiculo.getCliente());
            assertTrue(veiculo.belongsTo(cliente2));
            assertFalse(veiculo.belongsTo(cliente1));
        }
    }

    @Nested
    @DisplayName("Mutators e Acessors")
    class MutatorsTests {

        private Cliente createTestClient() {
            return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
        }

        @Test
        @DisplayName("Deve definir e obter marca")
        void shouldSetAndGetMarca() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            veiculo.setMarca("Honda");

            assertEquals("Honda", veiculo.getMarca());
        }

        @Test
        @DisplayName("Deve definir e obter modelo")
        void shouldSetAndGetModelo() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            veiculo.setModelo("Civic");

            assertEquals("Civic", veiculo.getModelo());
        }

        @Test
        @DisplayName("Deve definir e obter ano")
        void shouldSetAndGetAno() {
            Placa plate = new Placa("ABC1234");
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(plate, "Toyota", "Corolla", 2020, cliente);

            veiculo.setAno(2021);

            assertEquals(2021, veiculo.getAno());
        }
    }
}
