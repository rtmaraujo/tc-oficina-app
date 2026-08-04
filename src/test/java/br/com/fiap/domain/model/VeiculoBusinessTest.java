package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Vehicle - Testes de Métodos de Negócio")
class VeiculoBusinessTest {

    private Cliente createTestClient() {
        return new Cliente("Test Client", new CpfCnpj("12345678909"), "test@example.com", "11987654321");
    }

    @Nested
    @DisplayName("Atualização de Informações")
    class InfoUpdateTests {

        @Test
        @DisplayName("Deve atualizar informações do veículo corretamente")
        void shouldUpdateVehicleInfoCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2020, cliente);

            veiculo.atualizaVeiculoInfo("Honda", "Civic", 2022);

            assertEquals("Honda", veiculo.getMarca());
            assertEquals("Civic", veiculo.getModelo());
            assertEquals(2022, veiculo.getAno());
        }

        @Test
        @DisplayName("Deve lançar exceção para marca nula")
        void shouldThrowForNullBrand() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2020, cliente);

            assertThrows(IllegalArgumentException.class, () ->
                veiculo.atualizaVeiculoInfo(null, "Civic", 2022)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção para modelo vazio")
        void shouldThrowForEmptyModel() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2020, cliente);

            assertThrows(IllegalArgumentException.class, () ->
                veiculo.atualizaVeiculoInfo("Honda", "", 2022)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção para ano inválido")
        void shouldThrowForInvalidYear() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2020, cliente);

            assertThrows(IllegalArgumentException.class, () ->
                veiculo.atualizaVeiculoInfo("Honda", "Civic", 1800)
            );
        }
    }

    @Nested
    @DisplayName("Classificação por Idade")
    class AgeClassificationTests {

        @Test
        @DisplayName("Deve identificar veículo antigo corretamente")
        void shouldIdentifyOldVehicleCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo oldVeiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2005, cliente);
            Veiculo newVeiculo = new Veiculo(new Placa("XYZ9999"), "Honda", "Civic", 2020, cliente);

            assertTrue(oldVeiculo.isVeiculoAntigo());
            assertFalse(newVeiculo.isVeiculoAntigo());
        }

        @Test
        @DisplayName("Deve identificar veículo recente corretamente")
        void shouldIdentifyRecentVehicleCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo oldVeiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2015, cliente);
            Veiculo newVeiculo = new Veiculo(new Placa("XYZ9999"), "Honda", "Civic", 2022, cliente);

            assertTrue(newVeiculo.isVeiculoNovo());
            assertFalse(oldVeiculo.isVeiculoNovo());
        }

        @Test
        @DisplayName("Deve calcular idade do veículo corretamente")
        void shouldCalculateVehicleAgeCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2020, cliente);

            int expectedAge = 2026 - 2020; // Ano atual - ano do veículo
            assertEquals(expectedAge, veiculo.getIdadeVeiculo());
        }
    }

    @Nested
    @DisplayName("Descrição e Propriedade")
    class DescriptionAndOwnershipTests {

        @Test
        @DisplayName("Deve gerar descrição completa corretamente")
        void shouldGenerateFullDescriptionCorrectly() {
            Cliente cliente = createTestClient();
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2020, cliente);

            assertEquals("2020 Toyota Corolla", veiculo.getDescricaoGeral());
        }

        @Test
        @DisplayName("Deve verificar propriedade corretamente")
        void shouldCheckOwnershipCorrectly() {
            Cliente cliente1 = createTestClient();
            Veiculo veiculo = new Veiculo(new Placa("ABC1234"), "Toyota", "Corolla", 2020, cliente1);

            // Testa que o veículo pertence ao cliente que o criou
            assertTrue(veiculo.belongsTo(cliente1));
            
            // Testa que não pertence a null
            assertFalse(veiculo.belongsTo(null));
        }
    }
}
