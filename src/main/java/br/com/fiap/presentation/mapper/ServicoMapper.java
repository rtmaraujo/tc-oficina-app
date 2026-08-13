package br.com.fiap.presentation.mapper;

import br.com.fiap.domain.model.Servico;
import br.com.fiap.presentation.dto.response.ServicoDTO;
import br.com.fiap.presentation.dto.response.ServicoResumoDTO;

public class ServicoMapper {

    public static ServicoDTO toDTO(Servico servico) {
        if (servico == null) return null;
        return new ServicoDTO(
            servico.getId(),
            servico.getNome(),
            servico.getDescricao(),
            servico.getPreco(),
            servico.getTipoDeServico()
        );
    }

    public static ServicoResumoDTO toResumoDTO(Servico servico) {
        if (servico == null) return null;
        return new ServicoResumoDTO(
            servico.getId(),
            servico.getNome(),
            servico.getPreco()
        );
    }
}
