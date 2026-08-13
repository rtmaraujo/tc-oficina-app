package br.com.fiap.domain.model;

import br.com.fiap.util.ValidadorUtil;

import java.util.Objects;

public class CpfCnpj {
    private String value;

    public CpfCnpj(String value) {
        ValidadorUtil.validar(value);
        this.value = value.replaceAll("\\D", "");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CpfCnpj cpfCnpj = (CpfCnpj) o;
        return Objects.equals(value, cpfCnpj.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
