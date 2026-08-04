package br.com.fiap.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordem_servicos")
public class OrdemServicoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private VeiculoEntity veiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private br.com.fiap.domain.model.OrdemServicoStatus status;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "finalizado_em")
    private LocalDateTime finalizadoEm;

    @ManyToMany
    @JoinTable(
        name = "ordem_servicos_servicos",
        joinColumns = @JoinColumn(name = "ordem_servico_id"),
        inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<ServicoEntity> servicos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "ordem_servicos_pecas",
        joinColumns = @JoinColumn(name = "ordem_servico_id"),
        inverseJoinColumns = @JoinColumn(name = "peca_id")
    )
    private List<PecaEntity> pecas = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal totalOrcamento;

    protected OrdemServicoEntity() {}

    public OrdemServicoEntity(Long id, ClienteEntity cliente, VeiculoEntity veiculo,
                              br.com.fiap.domain.model.OrdemServicoStatus status,
                              LocalDateTime criadoEm, LocalDateTime finalizadoEm,
                              List<ServicoEntity> servicos, List<PecaEntity> pecas,
                              BigDecimal totalOrcamento) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.status = status;
        this.criadoEm = criadoEm;
        this.finalizadoEm = finalizadoEm;
        this.servicos = servicos != null ? servicos : new ArrayList<>();
        this.pecas = pecas != null ? pecas : new ArrayList<>();
        this.totalOrcamento = totalOrcamento;
    }

    public Long getId() { return id; }
    public ClienteEntity getCliente() { return cliente; }
    public VeiculoEntity getVeiculo() { return veiculo; }
    public br.com.fiap.domain.model.OrdemServicoStatus getStatus() { return status; }
    public void setStatus(br.com.fiap.domain.model.OrdemServicoStatus status) { this.status = status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getFinalizadoEm() { return finalizadoEm; }
    public void setFinalizadoEm(LocalDateTime finalizadoEm) { this.finalizadoEm = finalizadoEm; }
    public List<ServicoEntity> getServicos() { return servicos; }
    public List<PecaEntity> getPecas() { return pecas; }
    public BigDecimal getTotalOrcamento() { return totalOrcamento; }
    public void setTotalOrcamento(BigDecimal totalOrcamento) { this.totalOrcamento = totalOrcamento; }
}
