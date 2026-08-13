package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LicensePlate - Testes de Value Object de Placa")
class PlacaTest {

    @Nested
    @DisplayName("Criação e Validação de LicensePlate")
    class CreationTests {

        @Test
        @DisplayName("Deve criar LicensePlate com placa formato antigo válida")
        void shouldCreateWithValidOldFormat() {
            Placa plate = new Placa("ABC-1234");
            assertNotNull(plate);
            assertEquals("ABC1234", plate.getValue());
        }

        @Test
        @DisplayName("Deve criar LicensePlate com placa formato antigo sem formatação")
        void shouldCreateWithUnformattedOldFormat() {
            Placa plate = new Placa("ABC1234");
            assertNotNull(plate);
            assertEquals("ABC1234", plate.getValue());
        }

        @Test
        @DisplayName("Deve criar LicensePlate com placa Mercosul válida")
        void shouldCreateWithValidMercosulFormat() {
            Placa plate = new Placa("ABC1D23");
            assertNotNull(plate);
            assertEquals("ABC1D23", plate.getValue());
        }

        @Test
        @DisplayName("Deve criar LicensePlate com placa Mercosul com formatação")
        void shouldCreateWithFormattedMercosulFormat() {
            Placa plate = new Placa("ABC-1D23");
            assertNotNull(plate);
            assertEquals("ABC1D23", plate.getValue());
        }

        @Test
        @DisplayName("Deve converter para maiúsculas automaticamente")
        void shouldConvertToUppercase() {
            Placa plate = new Placa("abc1234");
            assertEquals("ABC1234", plate.getValue());
        }

        @Test
        @DisplayName("Deve remover espaços e caracteres especiais")
        void shouldRemoveSpacesAndSpecialChars() {
            Placa plate = new Placa("A B C - 1 2 3 4");
            assertEquals("ABC1234", plate.getValue());
        }

        @Test
        @DisplayName("Deve lançar exceção para placa nula")
        void shouldThrowForNullPlate() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Placa(null)
            );
            assertEquals("A placa não pode ser nula ou estar vazia.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para placa vazia")
        void shouldThrowForEmptyPlate() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Placa("")
            );
            assertEquals("A placa não pode ser nula ou estar vazia.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para placa com tamanho incorreto")
        void shouldThrowForWrongSize() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Placa("AB12")
            );
            assertEquals("Formato de placa inválido: deve conter 7 caracteres (3 letras + 4 números)", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"1234567", "ABCDEFG", "12AB34CD", "A1B2C3D4"})
        @DisplayName("Deve rejeitar placa com formato inválido")
        void shouldRejectInvalidFormat(String plate) {
            assertThrows(IllegalArgumentException.class, () -> new Placa(plate));
        }

        @Test
        @DisplayName("Deve lançar exceção para placa com apenas números")
        void shouldThrowForOnlyNumbers() {
            assertThrows(IllegalArgumentException.class, () -> new Placa("1234567"));
        }

        @Test
        @DisplayName("Deve lançar exceção para placa com apenas letras")
        void shouldThrowForOnlyLetters() {
            assertThrows(IllegalArgumentException.class, () -> new Placa("ABCDEFG"));
        }
    }

    @Nested
    @DisplayName("Igualdade e Hash")
    class EqualityTests {

        @Test
        @DisplayName("Deve ser igual a outra placa com mesmo valor")
        void shouldBeEqualToSameValue() {
            Placa plate1 = new Placa("ABC-1234");
            Placa plate2 = new Placa("ABC-1234");
            assertEquals(plate1, plate2);
        }

        @Test
        @DisplayName("Deve ser igual mesmo com formatações diferentes")
        void shouldBeEqualWithDifferentFormatting() {
            Placa plate1 = new Placa("ABC-1234");
            Placa plate2 = new Placa("ABC1234");
            assertEquals(plate1, plate2);
        }

        @Test
        @DisplayName("Deve ser igual mesmo com casos diferentes")
        void shouldBeEqualWithDifferentCase() {
            Placa plate1 = new Placa("ABC-1234");
            Placa plate2 = new Placa("abc-1234");
            assertEquals(plate1, plate2);
        }

        @Test
        @DisplayName("Deve ter mesmo hash para valores iguais")
        void shouldHaveSameHashForEqualValues() {
            Placa plate1 = new Placa("ABC-1234");
            Placa plate2 = new Placa("ABC-1234");
            assertEquals(plate1.hashCode(), plate2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente de outra placa com valor diferente")
        void shouldNotBeEqualToDifferentValue() {
            Placa plate1 = new Placa("ABC-1234");
            Placa plate2 = new Placa("XYZ-9999");
            assertNotEquals(plate1, plate2);
        }

        @Test
        @DisplayName("Deve ser diferente de null")
        void shouldNotEqualNull() {
            Placa plate = new Placa("ABC-1234");
            assertNotEquals(plate, null);
        }

        @Test
        @DisplayName("Deve ser diferente de objeto de outro tipo")
        void shouldNotEqualDifferentType() {
            Placa plate = new Placa("ABC-1234");
            assertNotEquals(plate, "ABC-1234");
        }

        @Test
        @DisplayName("Deve ser igual a si mesma")
        void shouldEqualItself() {
            Placa plate = new Placa("ABC-1234");
            assertEquals(plate, plate);
        }
    }

    @Nested
    @DisplayName("Representação String")
    class ToStringTests {

        @Test
        @DisplayName("Deve retornar valor como string")
        void shouldReturnValueAsString() {
            Placa plate = new Placa("ABC-1234");
            assertEquals("ABC1234", plate.toString());
        }

        @Test
        @DisplayName("Deve retornar valor limpo como string")
        void shouldReturnCleanedValueAsString() {
            Placa plate = new Placa("A-B-C-1-2-3-4");
            assertEquals("ABC1234", plate.toString());
        }
    }

    @Nested
    @DisplayName("Formatos Especiais")
    class SpecialFormatsTests {

        @Test
        @DisplayName("Deve aceitar Mercosul com letra no meio")
        void shouldAcceptMercosulLetter() {
            Placa plate = new Placa("ABC1D23");
            assertEquals("ABC1D23", plate.getValue());
        }

        @Test
        @DisplayName("Deve aceitar Mercosul com formatação variada")
        void shouldAcceptMercosulVariedFormatting() {
            Placa plate = new Placa("ABC - 1D23");
            assertEquals("ABC1D23", plate.getValue());
        }

        @Test
        @DisplayName("Deve rejeitar formato tipo Mercosul inválido")
        void shouldRejectInvalidMercosulFormat() {
            // Formato inválido: 3 letras + 2 números + 1 letra + 1 número
            assertThrows(IllegalArgumentException.class, () -> new Placa("ABC12D3"));
        }
    }
}

