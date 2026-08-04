package br.com.fiap.domain.service;

import br.com.fiap.domain.model.Peca;

import java.util.List;
import java.util.stream.Collectors;

public class InventarioService {

    public List<Peca> getPecasBaixoEstoque(List<Peca> pecas) {
        return pecas.stream()
            .filter(Peca::isBaixoEstoque)
            .collect(Collectors.toList());
    }

    public boolean podeCumprirPedido(List<Peca> requiredPecas) {
        return requiredPecas.stream()
            .allMatch(part -> part.podeCumprirPedido(1)); // Assume 1 unidade por peça
    }

    public void processarCumprimentoDePedidos(List<Peca> pecas) {
        for (Peca peca : pecas) {
            if (!peca.podeCumprirPedido(1)) {
                throw new IllegalStateException(
                    String.format("Estoque insuficiente para a peça: %s", peca.getNome())
                );
            }
            peca.removeEstoque(1);
        }
    }

    public void reabastecerPecas(List<Peca> pecas, int quantity) {
        for (Peca peca : pecas) {
            peca.addEstoque(quantity);
        }
    }

    public ResumoInventario getResumoInventario(List<Peca> pecas) {
        long totalPecas = pecas.size();
        long pecasDisponivel = pecas.stream().filter(Peca::isDisponivel).count();
        long pecasBaixoEstoque = pecas.stream().filter(Peca::isBaixoEstoque).count();
        long pecasForaDeEstoque = pecas.stream().filter(Peca::isForaDeEstoque).count();

        return new ResumoInventario(totalPecas, pecasDisponivel, pecasBaixoEstoque, pecasForaDeEstoque);
    }

    public static class ResumoInventario {
        private final long totalPecas;

        private final long pecasDisponivel;

        private final long pecasBaixoEstoque;

        private final long pecasForaDeEstoque;

        public ResumoInventario(long totalPecas, long pecasDisponivel,
                                long pecasBaixoEstoque, long pecasForaDeEstoque) {
            this.totalPecas = totalPecas;
            this.pecasDisponivel = pecasDisponivel;
            this.pecasBaixoEstoque = pecasBaixoEstoque;
            this.pecasForaDeEstoque = pecasForaDeEstoque;
        }

        public long getTotalPecas() { return totalPecas; }

        public long getPecasDisponivel() { return pecasDisponivel; }

        public long getPecasBaixoEstoque() { return pecasBaixoEstoque; }

        public long getPecasForaDeEstoque() { return pecasForaDeEstoque; }

        public double getPorcentagemDeDisponibilidade() {
            return totalPecas > 0 ? (double) pecasDisponivel / totalPecas * 100 : 0;
        }
    }
}
