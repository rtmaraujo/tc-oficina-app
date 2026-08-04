package br.com.fiap.domain.model;

import java.util.Objects;

public class Cliente {
    private Long id;
    private String nome;
    private CpfCnpj cpfCnpj;
    private String email;
    private String telefone;

    public Cliente(String nome, CpfCnpj cpfCnpj, String email, String telefone) {
        this(null, nome, cpfCnpj, email, telefone);
    }

    public Cliente(Long id, String nome, CpfCnpj cpfCnpj, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.telefone = telefone;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public CpfCnpj getCpfCnpj() { return cpfCnpj; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String phone) { this.telefone = phone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void atualizaContatoInfo(String email, String telefone) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone não pode ser nulo ou vazio");
        }
        this.email = email.trim();
        this.telefone = telefone.trim();
    }

    public void atualizaNome(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.nome = name.trim();
    }

    public boolean isPessoa() {
        return this.cpfCnpj.getValue().length() == 11;
    }

    public boolean isEmpresa() {
        return this.cpfCnpj.getValue().length() == 14;
    }

    public String getTipoDocumento() {
        return isPessoa() ? "CPF" : "CNPJ";
    }

    public String getDocumentoFormatado() {
        String value = this.cpfCnpj.getValue();
        if (isPessoa()) {
            // CPF: 123.456.789-09
            return value.substring(0, 3) + "." + value.substring(3, 6) + "." +
                   value.substring(6, 9) + "-" + value.substring(9);
        } else {
            // CNPJ: 11.222.333/0001-81
            return value.substring(0, 2) + "." + value.substring(2, 5) + "." +
                   value.substring(5, 8) + "/" + value.substring(8, 12) + "-" +
                   value.substring(12);
        }
    }
}
