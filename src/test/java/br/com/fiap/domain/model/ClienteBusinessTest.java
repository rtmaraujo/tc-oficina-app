package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Client - Testes de Métodos de Negócio")
class ClienteBusinessTest {

    @Nested
    @DisplayName("Atualização de Dados")
    class DataUpdateTests {

        @Test
        @DisplayName("Deve atualizar informações de contato corretamente")
        void shouldUpdateContactInfoCorrectly() {
            Cliente cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");

            cliente.atualizaContatoInfo("john.doe@example.com", "11999999999");

            assertEquals("john.doe@example.com", cliente.getEmail());
            assertEquals("11999999999", cliente.getTelefone());
        }

        @Test
        @DisplayName("Deve atualizar nome corretamente")
        void shouldUpdateNameCorrectly() {
            Cliente cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");

            cliente.atualizaNome("John Smith");

            assertEquals("John Smith", cliente.getNome());
        }

        @Test
        @DisplayName("Deve lançar exceção para email nulo na atualização")
        void shouldThrowForNullEmailInUpdate() {
            Cliente cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");

            assertThrows(IllegalArgumentException.class, () ->
                cliente.atualizaContatoInfo(null, "11999999999")
            );
        }

        @Test
        @DisplayName("Deve lançar exceção para telefone vazio na atualização")
        void shouldThrowForEmptyPhoneInUpdate() {
            Cliente cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");

            assertThrows(IllegalArgumentException.class, () ->
                cliente.atualizaContatoInfo("john@example.com", "")
            );
        }

        @Test
        @DisplayName("Deve lançar exceção para nome nulo na atualização")
        void shouldThrowForNullNameInUpdate() {
            Cliente cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");

            assertThrows(IllegalArgumentException.class, () ->
                cliente.atualizaNome(null)
            );
        }
    }

    @Nested
    @DisplayName("Identificação de Tipo de Pessoa")
    class PersonTypeTests {

        @Test
        @DisplayName("Deve identificar pessoa física corretamente")
        void shouldIdentifyPhysicalPersonCorrectly() {
            Cliente cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");

            assertTrue(cliente.isPessoa());
            assertFalse(cliente.isEmpresa());
            assertEquals("CPF", cliente.getTipoDocumento());
        }

        @Test
        @DisplayName("Deve identificar pessoa jurídica corretamente")
        void shouldIdentifyLegalEntityCorrectly() {
            Cliente cliente = new Cliente("Tech Corp", new CpfCnpj("11222333000181"), "contact@tech.com", "1133334444");

            assertTrue(cliente.isEmpresa());
            assertFalse(cliente.isPessoa());
            assertEquals("CNPJ", cliente.getTipoDocumento());
        }
    }

    @Nested
    @DisplayName("Formatação de Documento")
    class DocumentFormattingTests {

        @Test
        @DisplayName("Deve formatar CPF corretamente")
        void shouldFormatCpfCorrectly() {
            Cliente cliente = new Cliente("John Doe", new CpfCnpj("12345678909"), "john@example.com", "11987654321");

            assertEquals("123.456.789-09", cliente.getDocumentoFormatado());
        }

        @Test
        @DisplayName("Deve formatar CNPJ corretamente")
        void shouldFormatCnpjCorrectly() {
            Cliente cliente = new Cliente("Tech Corp", new CpfCnpj("11222333000181"), "contact@tech.com", "1133334444");

            assertEquals("11.222.333/0001-81", cliente.getDocumentoFormatado());
        }
    }
}
