package br.com.fiap.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Peca {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer qtdEstoque;

    public Peca(String nome, String descricao, BigDecimal preco, Integer qtdEstoque) {
        this(null, nome, descricao, preco, qtdEstoque);
    }

    public Peca(Long id, String nome, String descricao, BigDecimal preco, Integer qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String name) { this.nome = name; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String description) { this.descricao = description; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getQtdEstoque() { return qtdEstoque; }
    public void setQtdEstoque(Integer qtdEstoque) { this.qtdEstoque = qtdEstoque; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peca peca = (Peca) o;
        return Objects.equals(id, peca.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void atualizaPecaInfo(String nome, String descricao, BigDecimal preco) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ser nulo ou vazio");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preco nao pode ser negativo");
        }
        this.nome = nome.trim();
        this.descricao = descricao != null ? descricao.trim() : null;
        this.preco = preco;
    }

    public void addEstoque(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade nao pode ser negativo");
        }
        this.qtdEstoque += quantity;
    }

    public void removeEstoque(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade nao pode ser negativa");
        }
        if (quantity > this.qtdEstoque) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }
        this.qtdEstoque -= quantity;
    }

    public boolean isDisponivel() {
        return this.qtdEstoque > 0;
    }

    public boolean isBaixoEstoque() {
        return this.qtdEstoque > 0 && this.qtdEstoque <= 5;
    }

    public boolean isForaDeEstoque() {
        return this.qtdEstoque <= 0;
    }

    public boolean podeCumprirPedido(int requestedQuantity) {
        return this.qtdEstoque >= requestedQuantity;
    }

    public BigDecimal getValorTotal() {
        return this.preco.multiply(BigDecimal.valueOf(this.qtdEstoque));
    }
}
