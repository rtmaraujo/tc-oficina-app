package br.com.fiap.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Servico {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;

    public Servico(String nome, String descricao, BigDecimal preco) {
        this(null, nome, descricao, preco);
    }

    public Servico(Long id, String nome, String descricao, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }

    public void setPreco(BigDecimal preco) { this.preco = preco; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servico that = (Servico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void atualizaServicoInfo(String nome, String descricao, BigDecimal preco) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.nome = nome.trim();
        this.descricao = descricao != null ? descricao.trim() : null;
        this.preco = preco;
    }

    public boolean isAcimaDe500() {
        return this.preco.compareTo(BigDecimal.valueOf(500)) > 0;
    }

    public boolean isAbaixoDe50() {
        return this.preco.compareTo(BigDecimal.valueOf(50)) < 0;
    }

    public String getTipoDeServico() {
        String nome = this.nome.toLowerCase();
        if (nome.contains("troca") || nome.contains("substitui")) {
            return "REPLACEMENT";
        } else if (nome.contains("limpeza") || nome.contains("lavagem")) {
            return "CLEANING";
        } else if (nome.contains("diagnóstico") || nome.contains("verificação")) {
            return "DIAGNOSTIC";
        } else if (nome.contains("revisão") || nome.contains("manutenção")) {
            return "MAINTENANCE";
        } else {
            return "GENERAL";
        }
    }

    public boolean isTipoManutencao() {
        return "MAINTENANCE".equals(getTipoDeServico());
    }

    public boolean isTipoSubstituicao() {
        return "REPLACEMENT".equals(getTipoDeServico());
    }
}
