package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CpfCnpj - Testes de Value Object")
class CpfCnpjTest {

    @Nested
    @DisplayName("Criação e Validação de CpfCnpj")
    class CreationTests {

        @Test
        @DisplayName("Deve criar CpfCnpj com CPF válido")
        void shouldCreateWithValidCPF() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            assertNotNull(cpfCnpj);
            assertEquals("12345678909", cpfCnpj.getValue());
        }

        @Test
        @DisplayName("Deve criar CpfCnpj com CPF formatado")
        void shouldCreateWithFormattedCPF() {
            CpfCnpj cpfCnpj = new CpfCnpj("123.456.789-09");
            assertNotNull(cpfCnpj);
            assertEquals("12345678909", cpfCnpj.getValue());
        }

        @Test
        @DisplayName("Deve criar CpfCnpj com CNPJ válido")
        void shouldCreateWithValidCNPJ() {
            CpfCnpj cpfCnpj = new CpfCnpj("11222333000181");
            assertNotNull(cpfCnpj);
            assertEquals("11222333000181", cpfCnpj.getValue());
        }

        @Test
        @DisplayName("Deve criar CpfCnpj com CNPJ formatado")
        void shouldCreateWithFormattedCNPJ() {
            CpfCnpj cpfCnpj = new CpfCnpj("11.222.333/0001-81");
            assertNotNull(cpfCnpj);
            assertEquals("11222333000181", cpfCnpj.getValue());
        }

        @Test
        @DisplayName("Deve lançar exceção para CPF inválido")
        void shouldThrowForInvalidCPF() {
            assertThrows(IllegalArgumentException.class, () -> new CpfCnpj("12345678900"));
        }

        @Test
        @DisplayName("Deve lançar exceção para CNPJ inválido")
        void shouldThrowForInvalidCNPJ() {
            assertThrows(IllegalArgumentException.class, () -> new CpfCnpj("11222333000180"));
        }

        @Test
        @DisplayName("Deve lançar exceção para comprimento inválido")
        void shouldThrowForInvalidLength() {
            assertThrows(IllegalArgumentException.class, () -> new CpfCnpj("123456789"));
        }
    }

    @Nested
    @DisplayName("Igualdade e Hash")
    class EqualityTests {

        @Test
        @DisplayName("Deve ser igual a outro CpfCnpj com mesmo valor")
        void shouldBeEqualToSameValue() {
            CpfCnpj cpf1 = new CpfCnpj("12345678909");
            CpfCnpj cpf2 = new CpfCnpj("12345678909");
            assertEquals(cpf1, cpf2);
        }

        @Test
        @DisplayName("Deve ser igual mesmo com formatações diferentes")
        void shouldBeEqualWithDifferentFormatting() {
            CpfCnpj cpf1 = new CpfCnpj("123.456.789-09");
            CpfCnpj cpf2 = new CpfCnpj("12345678909");
            assertEquals(cpf1, cpf2);
        }

        @Test
        @DisplayName("Deve ter mesmo hash para valores iguais")
        void shouldHaveSameHashForEqualValues() {
            CpfCnpj cpf1 = new CpfCnpj("12345678909");
            CpfCnpj cpf2 = new CpfCnpj("12345678909");
            assertEquals(cpf1.hashCode(), cpf2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente de outro CpfCnpj com valor diferente")
        void shouldNotBeEqualToDifferentValue() {
            CpfCnpj cpf1 = new CpfCnpj("12345678909");
            CpfCnpj cpf2 = new CpfCnpj("11222333000181");
            assertNotEquals(cpf1, cpf2);
        }

        @Test
        @DisplayName("Deve ser diferente de null")
        void shouldNotEqualNull() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            assertNotEquals(cpfCnpj, null);
        }

        @Test
        @DisplayName("Deve ser diferente de objeto de outro tipo")
        void shouldNotEqualDifferentType() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            assertNotEquals(cpfCnpj, "12345678909");
        }

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void shouldEqualItself() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            assertEquals(cpfCnpj, cpfCnpj);
        }
    }

    @Nested
    @DisplayName("Representação String")
    class ToStringTests {

        @Test
        @DisplayName("Deve retornar valor como string")
        void shouldReturnValueAsString() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            assertEquals("12345678909", cpfCnpj.toString());
        }

        @Test
        @DisplayName("Deve retornar valor limpo como string")
        void shouldReturnCleanedValueAsString() {
            CpfCnpj cpfCnpj = new CpfCnpj("123.456.789-09");
            assertEquals("12345678909", cpfCnpj.toString());
        }
    }
}
