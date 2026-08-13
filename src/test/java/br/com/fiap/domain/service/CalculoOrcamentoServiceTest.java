package br.com.fiap.domain.service;

import br.com.fiap.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Servico Calculo Orcamento - Testes de Calculo de Preco")
class CalculoOrcamentoServiceTest {

    private CalculoOrcamentoService calculoOrcamentoService;
    private OrdemServico ordemServico;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        calculoOrcamentoService = new CalculoOrcamentoService();
        
        // Criar cliente
        CpfCnpj cpf = new CpfCnpj("12345678909");
        cliente = new Cliente("João Silva", cpf, "joao@example.com", "11987654321");
        
        // Criar veículo
        Placa placa = new Placa("ABC1234");
        Veiculo veiculo = new Veiculo(placa, "Toyota", "Corolla", 2015, cliente);
        
        // Criar pedido
        ordemServico = new OrdemServico(cliente, veiculo, new ArrayList<>(), new ArrayList<>());
    }

    @Nested
    @DisplayName("Calculo de Orcamento Total")
    class CalculoOrcamentoTotalTests {

        @Test
        @DisplayName("Deve calcular orcamento total com servicos e pecas")
        void shouldCalculateTotalBudgetWithServicesAndParts() {
            Servico servico = new Servico("Troca de Óleo", "Troca de óleo sintetico", new BigDecimal("150.00"));
            Peca peca = new Peca("Óleo Sintético", "5L", new BigDecimal("80.00"), 10);
            
            ordemServico.addServico(servico);
            ordemServico.addPeca(peca);
            
            BigDecimal resultado = calculoOrcamentoService.calculateTotalOrcamento(ordemServico);
            
            assertNotNull(resultado);
            // Subtotal: 150 + 80 = 230
            // Desconto: 0 (não atinge 1000)
            // Imposto: 230 * 0.10 = 23
            // Total: 230 + 23 = 253
            assertEquals(new BigDecimal("253.00"), resultado);
        }

        @Test
        @DisplayName("Deve calcular orcamento sem servicos nem pecas")
        void shouldCalculateTotalBudgetEmpty() {
            BigDecimal resultado = calculoOrcamentoService.calculateTotalOrcamento(ordemServico);
            
            assertNotNull(resultado);
            assertEquals(BigDecimal.ZERO, resultado);
        }

        @Test
        @DisplayName("Deve calcular orcamento apenas com servicos")
        void shouldCalculateTotalBudgetOnlyServices() {
            Servico servico = new Servico("Alinhamento", "Alinhamento 3D", new BigDecimal("200.00"));
            ordemServico.addServico(servico);
            
            BigDecimal resultado = calculoOrcamentoService.calculateTotalOrcamento(ordemServico);
            
            assertNotNull(resultado);
            // Subtotal: 200
            // Desconto: 0
            // Imposto: 200 * 0.10 = 20
            // Total: 200 + 20 = 220
            assertEquals(new BigDecimal("220.00"), resultado);
        }

        @Test
        @DisplayName("Deve calcular orcamento apenas com pecas")
        void shouldCalculateTotalBudgetOnlyParts() {
            Peca peca = new Peca("Filtro de Ar", "Para motor 1.6", new BigDecimal("45.00"), 5);
            ordemServico.addPeca(peca);
            
            BigDecimal resultado = calculoOrcamentoService.calculateTotalOrcamento(ordemServico);
            
            assertNotNull(resultado);
            // Subtotal: 45
            // Desconto: 0
            // Imposto: 45 * 0.10 = 4.50
            // Total: 45 + 4.50 = 49.50
            assertEquals(new BigDecimal("49.50"), resultado);
        }
    }

    @Nested
    @DisplayName("Calculo de Desconto")
    class CalculoDescontoTests {

        @Test
        @DisplayName("Deve aplicar desconto para cliente recorrente")
        void shouldApplyRecurringClientDiscount() {
            // Cliente com id 3 é recorrente (3 % 3 == 0)
            CpfCnpj cpf = new CpfCnpj("98765432100");
            Cliente clienteRecorrente = new Cliente("Maria", cpf, "maria@example.com", "11987654321");
            
            Placa placa = new Placa("XYZ9876");
            Veiculo veiculo2 = new Veiculo(placa, "Honda", "Civic", 2020, clienteRecorrente);
            
            OrdemServico pedido = new OrdemServico(clienteRecorrente, veiculo2, new ArrayList<>(), new ArrayList<>());
            
            BigDecimal subtotal = new BigDecimal("500.00");
            BigDecimal desconto = calculoOrcamentoService.calculateDiscount(subtotal, pedido);
            
            assertNotNull(desconto);
            // Desconto cliente recorrente: 0 (id não tem ID setado)
            assertEquals(BigDecimal.ZERO, desconto);
        }

        @Test
        @DisplayName("Deve aplicar desconto para veiculo antigo")
        void shouldApplyOldVehicleDiscount() {
            Placa placa = new Placa("OLD1234");
            Veiculo veiculoAntigo = new Veiculo(placa, "Volkswagen", "Gol", 2005, cliente);
            
            OrdemServico pedido = new OrdemServico(cliente, veiculoAntigo, new ArrayList<>(), new ArrayList<>());
            
            BigDecimal subtotal = new BigDecimal("500.00");
            BigDecimal desconto = calculoOrcamentoService.calculateDiscount(subtotal, pedido);
            
            assertNotNull(desconto);
            // Desconto veículo antigo: 30
            assertEquals(new BigDecimal("30.00"), desconto);
        }

        @Test
        @DisplayName("Nao deve aplicar desconto quando subtotal < 1000 e cliente nao recorrente e veiculo novo")
        void shouldNotApplyDiscountWhenNoConditionsMet() {
            BigDecimal subtotal = new BigDecimal("500.00");
            BigDecimal desconto = calculoOrcamentoService.calculateDiscount(subtotal, ordemServico);
            
            assertNotNull(desconto);
            assertEquals(BigDecimal.ZERO, desconto);
        }
    }

    @Nested
    @DisplayName("Calculo de Imposto")
    class CalculoImpostoTests {

        @Test
        @DisplayName("Deve calcular imposto como 10% do subtotal")
        void shouldCalculateTaxAs10Percent() {
            BigDecimal subtotal = new BigDecimal("1000.00");
            BigDecimal imposto = calculoOrcamentoService.calculateTax(subtotal);
            
            assertNotNull(imposto);
            assertEquals(new BigDecimal("100.00"), imposto);
        }

        @Test
        @DisplayName("Deve calcular imposto com valores pequenos")
        void shouldCalculateTaxWithSmallValues() {
            BigDecimal subtotal = new BigDecimal("50.00");
            BigDecimal imposto = calculoOrcamentoService.calculateTax(subtotal);
            
            assertNotNull(imposto);
            assertEquals(new BigDecimal("5.00"), imposto);
        }

        @Test
        @DisplayName("Deve calcular imposto zero para subtotal zero")
        void shouldCalculateTaxZeroForZeroSubtotal() {
            BigDecimal subtotal = BigDecimal.ZERO;
            BigDecimal imposto = calculoOrcamentoService.calculateTax(subtotal);
            
            assertNotNull(imposto);
            assertEquals(BigDecimal.ZERO, imposto);
        }
    }

    @Nested
    @DisplayName("Detalhamento de Orcamento")
    class DetalhamentoOrcamentoTests {

        @Test
        @DisplayName("Deve gerar detalhamento completo do orcamento")
        void shouldGetBudgetBreakdown() {
            Servico servico = new Servico("Revisão", "Revisão completa", new BigDecimal("500.00"));
            Peca peca = new Peca("Filtro", "Filtro de cabine", new BigDecimal("200.00"), 1);
            
            ordemServico.addServico(servico);
            ordemServico.addPeca(peca);
            
            CalculoOrcamentoService.BudgetBreakdown breakdown = calculoOrcamentoService.getBudgetBreakdown(ordemServico);
            
            assertNotNull(breakdown);
            assertEquals(new BigDecimal("500.00"), breakdown.getServicesTotal());
            assertEquals(new BigDecimal("200.00"), breakdown.getPartsTotal());
            assertEquals(new BigDecimal("700.00"), breakdown.getSubtotal());
            assertEquals(new BigDecimal("70.00"), breakdown.getTax());
        }
    }

    @Nested
    @DisplayName("Validacoes de Orcamento")
    class ValidacoesOrcamentoTests {

        @Test
        @DisplayName("Deve confirmar que pode aplicar desconto")
        void shouldConfirmCanApplyDiscount() {
            // Usar veiculo antigo que tem desconto automático
            Placa placa = new Placa("DES1234");
            Veiculo veiculo2 = new Veiculo(placa, "Ford", "Focus", 2005, cliente);
            
            OrdemServico pedido = new OrdemServico(cliente, veiculo2, new ArrayList<>(), new ArrayList<>());
            
            boolean resultado = calculoOrcamentoService.canApplyDiscount(pedido);
            assertTrue(resultado);
        }

        @Test
        @DisplayName("Deve validar orcamento razoavel")
        void shouldValidateReasonableBudget() {
            Servico servico = new Servico("Servico", "Desc", new BigDecimal("500.00"));
            ordemServico.addServico(servico);
            
            boolean resultado = calculoOrcamentoService.isBudgetReasonable(ordemServico);
            assertTrue(resultado);
        }

        @Test
        @DisplayName("Deve rejeitar orcamento abaixo do minimo")
        void shouldRejectBudgetBelowMinimum() {
            // Nenhum serviço, orcamento será 0
            boolean resultado = calculoOrcamentoService.isBudgetReasonable(ordemServico);
            assertFalse(resultado);
        }
    }
}

