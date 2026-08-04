package br.com.fiap.domain.service;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.OrdemServico;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoOrcamentoService {

    private static final BigDecimal DISCOUNT_THRESHOLD = BigDecimal.valueOf(1000);

    private static final BigDecimal DISCOUNT_PERCENTAGE = BigDecimal.valueOf(0.05);

    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.10);
    
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public BigDecimal calculateTotalOrcamento(OrdemServico ordemServico) {
        BigDecimal servicesTotal = ensureScale(ordemServico.getTotalServicos());
        BigDecimal partsTotal = ensureScale(ordemServico.getTotalPecas());
        BigDecimal subtotal = servicesTotal.add(partsTotal);

        BigDecimal discount = calculateDiscount(subtotal, ordemServico);
        BigDecimal tax = calculateTax(subtotal);

        BigDecimal result = subtotal.subtract(discount).add(tax);
        return result.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : result;
    }

    public BigDecimal calculateDiscount(BigDecimal subtotal, OrdemServico ordemServico) {
        BigDecimal discount = BigDecimal.ZERO;

        // Desconto por valor alto (volume)
        if (subtotal.compareTo(DISCOUNT_THRESHOLD) >= 0) {
            discount = discount.add(subtotal.multiply(DISCOUNT_PERCENTAGE));
        }

        // Desconto para clientes recorrentes (simulação)
        if (isRecurringClient(ordemServico.getCliente())) {
            discount = discount.add(BigDecimal.valueOf(50));
        }

        // Desconto para veículos antigos
        if (ordemServico.getVeiculo().isVeiculoAntigo()) {
            discount = discount.add(BigDecimal.valueOf(30));
        }

        return discount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : ensureScale(discount);
    }

    public BigDecimal calculateTax(BigDecimal subtotal) {
        if (subtotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return ensureScale(subtotal.multiply(TAX_RATE));
    }

    public BudgetBreakdown getBudgetBreakdown(OrdemServico ordemServico) {
        BigDecimal servicesTotal = ordemServico.getTotalServicos().setScale(SCALE, ROUNDING_MODE);
        BigDecimal partsTotal = ordemServico.getTotalPecas().setScale(SCALE, ROUNDING_MODE);
        BigDecimal subtotal = servicesTotal.add(partsTotal).setScale(SCALE, ROUNDING_MODE);
        BigDecimal discount = calculateDiscount(subtotal, ordemServico);
        BigDecimal tax = calculateTax(subtotal);
        BigDecimal total = subtotal.subtract(discount).add(tax).setScale(SCALE, ROUNDING_MODE);

        return new BudgetBreakdown(servicesTotal, partsTotal, subtotal, discount, tax, total);
    }

    public boolean canApplyDiscount(OrdemServico ordemServico) {
        BigDecimal subtotal = ordemServico.getTotalServicos().add(ordemServico.getTotalPecas());
        return subtotal.compareTo(DISCOUNT_THRESHOLD) >= 0 ||
               isRecurringClient(ordemServico.getCliente()) ||
               ordemServico.getVeiculo().isVeiculoAntigo();
    }

    public boolean isBudgetReasonable(OrdemServico ordemServico) {
        BigDecimal total = calculateTotalOrcamento(ordemServico);
        return total.compareTo(BigDecimal.valueOf(50)) >= 0 &&
               total.compareTo(BigDecimal.valueOf(10000)) <= 0;
    }

    private boolean isRecurringClient(Cliente cliente) {
        return cliente.getId() != null && cliente.getId() % 3 == 0;
    }

    private BigDecimal ensureScale(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return value.setScale(SCALE, ROUNDING_MODE);
    }

    public static class BudgetBreakdown {
        private final BigDecimal servicesTotal;

        private final BigDecimal partsTotal;

        private final BigDecimal subtotal;

        private final BigDecimal discount;

        private final BigDecimal tax;

        private final BigDecimal total;

        public BudgetBreakdown(BigDecimal servicesTotal, BigDecimal partsTotal,
                             BigDecimal subtotal, BigDecimal discount,
                             BigDecimal tax, BigDecimal total) {
            this.servicesTotal = servicesTotal;
            this.partsTotal = partsTotal;
            this.subtotal = subtotal;
            this.discount = discount;
            this.tax = tax;
            this.total = total;
        }

        public BigDecimal getServicesTotal() { return servicesTotal; }

        public BigDecimal getPartsTotal() { return partsTotal; }

        public BigDecimal getSubtotal() { return subtotal; }

        public BigDecimal getDiscount() { return discount; }

        public BigDecimal getTax() { return tax; }

        public BigDecimal getTotal() { return total; }
    }
}
