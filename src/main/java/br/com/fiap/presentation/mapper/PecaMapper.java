package br.com.fiap.presentation.mapper;

import br.com.fiap.domain.model.Peca;
import br.com.fiap.presentation.dto.response.PecaDTO;
import br.com.fiap.presentation.dto.response.PecaResumoDTO;

public class PecaMapper {

    public static PecaDTO toDTO(Peca peca) {
        if (peca == null) return null;
        return new PecaDTO(
            peca.getId(),
            peca.getNome(),
            peca.getDescricao(),
            peca.getPreco(),
            peca.getQtdEstoque()
        );
    }

    public static PecaResumoDTO toResumoDTO(Peca peca) {
        if (peca == null) return null;
        return new PecaResumoDTO(
            peca.getId(),
            peca.getNome(),
            peca.getPreco(),
            peca.getQtdEstoque()
        );
    }
}
