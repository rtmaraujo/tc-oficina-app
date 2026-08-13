package br.com.fiap.domain.model;

import java.util.Objects;

public class Veiculo {
    private Long id;
    private Placa placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private Cliente cliente;

    public Veiculo(Placa placa, String marca, String modelo, Integer ano, Cliente cliente) {
        this(null, placa, marca, modelo, ano, cliente);
    }

    public Veiculo(Long id, Placa placa, String marca, String modelo, Integer ano, Cliente cliente) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cliente = cliente;
    }

    public Long getId() { return id; }

    public Placa getPlaca() { return placa; }

    public String getMarca() { return marca; }

    public void setMarca(String brand) { this.marca = brand; }

    public String getModelo() { return modelo; }

    public void setModelo(String model) { this.modelo = model; }

    public Integer getAno() { return ano; }

    public void setAno(Integer year) { this.ano = year; }

    public Cliente getCliente() { return cliente; }

    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(id, veiculo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void atualizaVeiculoInfo(String marca, String modelo, Integer ano) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("Marca não pode ser nula ou vazia");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("Modelo não pode ser nulo ou vazio");
        }
        if (ano == null || ano < 1900 || ano > 2030) {
            throw new IllegalArgumentException("Ano deve estar entre 1900 e 2030");
        }
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.ano = ano;
    }

    public boolean isVeiculoAntigo() {
        return this.ano < 2010;
    }

    public boolean isVeiculoNovo() {
        return this.ano >= 2020;
    }

    public int getIdadeVeiculo() {
        return java.time.Year.now().getValue() - this.ano;
    }

    public String getDescricaoGeral() {
        return String.format("%d %s %s", ano, marca, modelo);
    }

    public boolean belongsTo(Cliente cliente) {
        return this.cliente != null && this.cliente.equals(cliente);
    }
}
