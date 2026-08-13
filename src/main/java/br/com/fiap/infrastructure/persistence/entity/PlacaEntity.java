package br.com.fiap.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class PlacaEntity {
    private String value;

    protected PlacaEntity() {}

    public PlacaEntity(String value) { this.value = value; }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacaEntity that = (PlacaEntity) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }
}
