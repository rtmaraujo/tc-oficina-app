package br.com.fiap.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Client - Testes de Entidade de Domínio")
class ClienteTest {

    @Nested
    @DisplayName("Criação e Validação de Client")
    class CreationTests {

        @Test
        @DisplayName("Deve criar cliente com dados válidos e CPF")
        void shouldCreateClientWithValidCPF() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            Cliente cliente = new Cliente("John Doe", cpfCnpj, "john@example.com", "11987654321");

            assertNotNull(cliente);
            assertEquals("John Doe", cliente.getNome());
            assertEquals(cpfCnpj, cliente.getCpfCnpj());
            assertEquals("john@example.com", cliente.getEmail());
            assertEquals("11987654321", cliente.getTelefone());
        }

        @Test
        @DisplayName("Deve criar cliente com CNPJ")
        void shouldCreateClientWithValidCNPJ() {
            CpfCnpj cnpj = new CpfCnpj("11222333000181");
            Cliente cliente = new Cliente("Tech Company", cnpj, "contact@tech.com", "1133334444");

            assertNotNull(cliente);
            assertEquals("Tech Company", cliente.getNome());
            assertEquals(cnpj, cliente.getCpfCnpj());
        }
    }

    @Nested
    @DisplayName("Igualdade e Identificação de Clientes")
    class EqualityTests {

        @Test
        @DisplayName("Deve ser diferente de null")
        void shouldNotEqualNull() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            Cliente cliente = new Cliente("John Doe", cpfCnpj, "john@example.com", "11987654321");
            assertNotEquals(cliente, null);
        }

        @Test
        @DisplayName("Deve ser diferente de objeto de outro tipo")
        void shouldNotEqualDifferentType() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            Cliente cliente = new Cliente("John Doe", cpfCnpj, "john@example.com", "11987654321");
            assertNotEquals(cliente, "John Doe");
        }
    }

    @Nested
    @DisplayName("Dados Pessoais do Cliente")
    class PersonalDataTests {

        @Test
        @DisplayName("Deve armazenar todos os dados pessoais corretamente")
        void shouldStoreAllPersonalDataCorrectly() {
            CpfCnpj cpfCnpj = new CpfCnpj("12345678909");
            String name = "John Doe";
            String email = "john@example.com";
            String phone = "11987654321";
            
            Cliente cliente = new Cliente(name, cpfCnpj, email, phone);

            assertEquals(name, cliente.getNome());
            assertEquals(cpfCnpj, cliente.getCpfCnpj());
            assertEquals(email, cliente.getEmail());
            assertEquals(phone, cliente.getTelefone());
        }

        @Test
        @DisplayName("Deve manter dados intactos após criação")
        void shouldKeepDataIntactAfterCreation() {
            CpfCnpj cpfCnpj = new CpfCnpj("11222333000181");
            Cliente cliente = new Cliente("Tech Corp", cpfCnpj, "contact@tech.com", "1133334444");

            // Verifica que os dados não foram alterados
            assertEquals("Tech Corp", cliente.getNome());
            assertEquals(cpfCnpj.getValue(), cliente.getCpfCnpj().getValue());
            assertEquals("contact@tech.com", cliente.getEmail());
        }
    }

    @Nested
    @DisplayName("Identificação de Pessoa Física vs Jurídica")
    class ClienteTypeTests {

        @Test
        @DisplayName("Cliente com 11 dígitos deve ser pessoa física")
        void shouldIdentifyPhysicalPerson() {
            CpfCnpj cpf = new CpfCnpj("12345678909");
            Cliente cliente = new Cliente("John Doe", cpf, "john@example.com", "11987654321");

            assertEquals(11, cliente.getCpfCnpj().getValue().length());
        }

        @Test
        @DisplayName("Cliente com 14 dígitos deve ser pessoa jurídica")
        void shouldIdentifyLegalEntity() {
            CpfCnpj cnpj = new CpfCnpj("11222333000181");
            Cliente cliente = new Cliente("Tech Company", cnpj, "contact@tech.com", "1133334444");

            assertEquals(14, cliente.getCpfCnpj().getValue().length());
        }
    }
}
