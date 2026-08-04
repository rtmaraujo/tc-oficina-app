package br.com.fiap.domain.model;

import br.com.fiap.domain.service.CalculoOrcamentoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdemServico {
    private Long id;
    private Cliente cliente;
    private Veiculo veiculo;
    private OrdemServicoStatus status;
    private LocalDateTime criadoEm;
    private LocalDateTime finalizadoEm;
    private List<Servico> servicos = new ArrayList<>();
    private List<Peca> pecas = new ArrayList<>();
    private BigDecimal totalOrcamento;

    public OrdemServico(Cliente cliente, Veiculo veiculo, List<Servico> servicos, List<Peca> pecas) {
        this(null, cliente, veiculo, servicos, pecas, LocalDateTime.now());
    }

    public OrdemServico(Cliente cliente, Veiculo veiculo, List<Servico> servicos, List<Peca> pecas, LocalDateTime criadoEm) {
        this(null, cliente, veiculo, servicos, pecas, criadoEm);
    }

    public OrdemServico(Long id, Cliente cliente, Veiculo veiculo, List<Servico> servicos, List<Peca> pecas, LocalDateTime criadoEm) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.servicos = servicos != null ? servicos : new ArrayList<>();
        this.pecas = pecas != null ? pecas : new ArrayList<>();
        this.status = OrdemServicoStatus.RECEBIDA;
        this.criadoEm = criadoEm;
        this.totalOrcamento = calculaOrcamento();
    }

    private BigDecimal calculaOrcamento() {
        BigDecimal total = BigDecimal.ZERO;
        for (Servico s : servicos) {
            total = total.add(s.getPreco());
        }
        for (Peca p : pecas) {
            total = total.add(p.getPreco());
        }
        return total;
    }

    public void atualizaStatus(OrdemServicoStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status nao pode ser nulo");
        }
        this.status = newStatus;
        // Marcar como finalizado quando status muda para ENTREGUE
        if (newStatus == OrdemServicoStatus.ENTREGUE && this.finalizadoEm == null) {
            this.finalizadoEm = LocalDateTime.now();
        }
    }

    public boolean canAdvanceStatus() {
        return this.status != OrdemServicoStatus.ENTREGUE;
    }

    public boolean isCompleto() {
        return this.status == OrdemServicoStatus.ENTREGUE;
    }

    public boolean isPendenteDeAprovacao() {
        return this.status == OrdemServicoStatus.AGUARDANDO_APROVACAO;
    }

    public void addServico(Servico service) {
        if (service == null) {
            throw new IllegalArgumentException("Servico nao pode ser nulo");
        }
        if (!this.servicos.contains(service)) {
            this.servicos.add(service);
            recalculaOrcamento();
        }
    }

    public void addPeca(Peca peca) {
        if (peca == null) {
            throw new IllegalArgumentException("Peca nao pode ser nulo");
        }
        if (!this.pecas.contains(peca)) {
            this.pecas.add(peca);
            recalculaOrcamento();
        }
    }

    public void removeServico(Servico servico) {
        if (servico != null && this.servicos.remove(servico)) {
            recalculaOrcamento();
        }
    }

    public void removePeca(Peca peca) {
        if (peca != null && this.pecas.remove(peca)) {
            recalculaOrcamento();
        }
    }

    private void recalculaOrcamento() {
        this.totalOrcamento = calculaOrcamento();
    }

    public BigDecimal getTotalServicos() {
        return servicos.stream()
            .map(Servico::getPreco)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalPecas() {
        return pecas.stream()
            .map(Peca::getPreco)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalItems() {
        return servicos.size() + pecas.size();
    }

    public boolean hasItems() {
        return !servicos.isEmpty() || !pecas.isEmpty();
    }

    // Getters
    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Veiculo getVeiculo() { return veiculo; }
    public OrdemServicoStatus getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getFinalizadoEm() { return finalizadoEm; }
    public void setFinalizadoEm(LocalDateTime finalizadoEm) { this.finalizadoEm = finalizadoEm; }
    public List<Servico> getServicos() { return servicos; }
    public List<Peca> getPecas() { return pecas; }
    public BigDecimal getTotalOrcamento() { return totalOrcamento; }
    public void setTotalOrcamento(BigDecimal totalOrcamento) { this.totalOrcamento = totalOrcamento; }

    public void recalculateBudget(CalculoOrcamentoService budgetService) {
        this.totalOrcamento = budgetService.calculateTotalOrcamento(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemServico that = (OrdemServico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
