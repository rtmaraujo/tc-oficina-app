package br.com.fiap.domain.model;

import br.com.fiap.util.ValidadorUtil;

import java.util.Objects;

public class Placa {
    private String value;

    public Placa(String value) {
        ValidadorUtil.validarPlaca(value);
        this.value = value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placa that = (Placa) o;
        return Objects.equals(value, that.value);
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
