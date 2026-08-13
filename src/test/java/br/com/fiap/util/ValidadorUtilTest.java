package br.com.fiap.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidadorUtil - Testes de Validação de CPF/CNPJ e Placa")
class ValidadorUtilTest {

    @Nested
    @DisplayName("Validação de CPF")
    class ValidarCPFTests {

        @Test
        @DisplayName("Deve aceitar CPF válido")
        void shouldAcceptValidCPF() {
            // CPF válido: 123.456.789-09
            assertDoesNotThrow(() -> ValidadorUtil.validar("12345678909"));
        }

        @Test
        @DisplayName("Deve aceitar CPF com formatação")
        void shouldAcceptFormattedCPF() {
            assertDoesNotThrow(() -> ValidadorUtil.validar("123.456.789-09"));
        }

        @Test
        @DisplayName("Deve rejeitar CPF nulo")
        void shouldRejectNullCPF() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validar(null)
            );
            assertEquals("O CPF/CNPJ não pode ser nulo ou vazio.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve rejeitar CPF vazio")
        void shouldRejectEmptyCPF() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validar("")
            );
            assertEquals("O CPF/CNPJ não pode ser nulo ou vazio.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve rejeitar CPF com tamanho incorreto")
        void shouldRejectWrongSizedCPF() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validar("1234567890")
            );
            assertEquals("Formato CPF/CNPJ inválido: deve ter 11 ou 14 dígitos.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve rejeitar CPF com dígitos verificadores inválidos")
        void shouldRejectCPFWithInvalidCheckDigits() {
            // CPF válido seria 123.456.789-09, mas alteramos o último dígito
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validar("12345678900")
            );
            assertEquals("CPF inválido: os dígitos de verificação não correspondem.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validação de CNPJ")
    class ValidarCNPJTests {

        @Test
        @DisplayName("Deve aceitar CNPJ válido")
        void shouldAcceptValidCNPJ() {
            // CNPJ válido: 11.222.333/0001-81
            assertDoesNotThrow(() -> ValidadorUtil.validar("11222333000181"));
        }

        @Test
        @DisplayName("Deve aceitar CNPJ com formatação")
        void shouldAcceptFormattedCNPJ() {
            assertDoesNotThrow(() -> ValidadorUtil.validar("11.222.333/0001-81"));
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com tamanho incorreto")
        void shouldRejectWrongSizedCNPJ() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validar("112223330001")
            );
            assertEquals("Formato CPF/CNPJ inválido: deve ter 11 ou 14 dígitos.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com dígitos verificadores inválidos")
        void shouldRejectCNPJWithInvalidCheckDigits() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validar("11222333000180")
            );
            assertEquals("CNPJ inválido: os dígitos de verificação não coincidem.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validação de Placa de Veículo")
    class ValidarPlacaTests {

        @Test
        @DisplayName("Deve aceitar placa formato antigo (ABC-1234)")
        void shouldAcceptOldFormatPlate() {
            assertDoesNotThrow(() -> ValidadorUtil.validarPlaca("ABC-1234"));
        }

        @Test
        @DisplayName("Deve aceitar placa formato antigo sem formatação (ABC1234)")
        void shouldAcceptOldFormatPlateUnformatted() {
            assertDoesNotThrow(() -> ValidadorUtil.validarPlaca("ABC1234"));
        }

        @Test
        @DisplayName("Deve aceitar placa formato Mercosul (ABC1D23)")
        void shouldAcceptMercosulFormatPlate() {
            assertDoesNotThrow(() -> ValidadorUtil.validarPlaca("ABC1D23"));
        }

        @Test
        @DisplayName("Deve aceitar placa Mercosul com formatação (ABC-1D23)")
        void shouldAcceptMercosulFormatPlateFormatted() {
            assertDoesNotThrow(() -> ValidadorUtil.validarPlaca("ABC-1D23"));
        }

        @Test
        @DisplayName("Deve rejeitar placa nula")
        void shouldRejectNullPlate() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validarPlaca(null)
            );
            assertEquals("A placa não pode ser nula ou estar vazia.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve rejeitar placa vazia")
        void shouldRejectEmptyPlate() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validarPlaca("")
            );
            assertEquals("A placa não pode ser nula ou estar vazia.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve rejeitar placa com tamanho incorreto")
        void shouldRejectWrongSizedPlate() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidadorUtil.validarPlaca("AB12")
            );
            assertEquals("Formato de placa inválido: deve conter 7 caracteres (3 letras + 4 números)", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"1234567", "ABCDEFG", "12AB34CD"})
        @DisplayName("Deve rejeitar placa com formato inválido")
        void shouldRejectInvalidFormatPlate(String plate) {
            assertThrows(IllegalArgumentException.class, () -> ValidadorUtil.validarPlaca(plate));
        }

        @Test
        @DisplayName("Deve aceitar placa com espaços e caracteres especiais")
        void shouldAcceptPlateWithSpacesAndSpecialChars() {
            // Deve limpar e validar
            assertDoesNotThrow(() -> ValidadorUtil.validarPlaca("A B C - 1 2 3 4"));
        }
    }

    @Nested
    @DisplayName("Casos de Borda e Integração")
    class EdgeCasesTests {

        @Test
        @DisplayName("Deve diferenciar entre CPF e CNPJ automaticamente")
        void shouldDifferentiateBetweenCPFAndCNPJ() {
            // CPF válido
            assertDoesNotThrow(() -> ValidadorUtil.validar("12345678909"));
            
            // CNPJ válido
            assertDoesNotThrow(() -> ValidadorUtil.validar("11222333000181"));
        }

        @Test
        @DisplayName("Deve aceitar CPF/CNPJ com espaços")
        void shouldAcceptCPFCNPJWithSpaces() {
            assertDoesNotThrow(() -> ValidadorUtil.validar("123 456 789 09"));
            assertDoesNotThrow(() -> ValidadorUtil.validar("11 222 333 0001 81"));
        }

        @Test
        @DisplayName("Deve rejeitar número com comprimento inválido")
        void shouldRejectInvalidLength() {
            assertThrows(IllegalArgumentException.class, () -> ValidadorUtil.validar("123456789"));
        }
    }
}

