package br.com.fiap.presentation.mapper;

import br.com.fiap.domain.model.OrdemServico;
import br.com.fiap.domain.model.Servico;
import br.com.fiap.domain.model.Peca;
import br.com.fiap.presentation.dto.response.OrdemServicoDTO;
import br.com.fiap.presentation.dto.response.OrdemServicoStatusDTO;
import br.com.fiap.presentation.dto.response.ServicoResumoDTO;
import br.com.fiap.presentation.dto.response.PecaResumoDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrdemServicoMapper {

    public static OrdemServicoDTO toDTO(OrdemServico os) {
        if (os == null) return null;
        List<ServicoResumoDTO> servicos = os.getServicos() != null
            ? os.getServicos().stream().map(OrdemServicoMapper::toServicoResumo).toList()
            : Collections.emptyList();
        List<PecaResumoDTO> pecas = os.getPecas() != null
            ? os.getPecas().stream().map(OrdemServicoMapper::toPecaResumo).toList()
            : Collections.emptyList();
        return new OrdemServicoDTO(
            os.getId(),
            os.getCliente() != null ? os.getCliente().getId() : null,
            os.getCliente() != null ? os.getCliente().getNome() : null,
            os.getVeiculo() != null ? os.getVeiculo().getId() : null,
            os.getVeiculo() != null ? os.getVeiculo().getPlaca().getValue() : null,
            os.getStatus() != null ? os.getStatus().name() : null,
            os.getCriadoEm(),
            os.getFinalizadoEm(),
            servicos,
            pecas,
            os.getTotalOrcamento()
        );
    }

    public static OrdemServicoStatusDTO toStatusDTO(OrdemServico os) {
        if (os == null) return null;
        return new OrdemServicoStatusDTO(
            os.getId(),
            os.getStatus() != null ? os.getStatus().name() : null,
            os.getTotalOrcamento(),
            os.getFinalizadoEm() != null ? os.getFinalizadoEm() : os.getCriadoEm()
        );
    }

    private static ServicoResumoDTO toServicoResumo(Servico s) {
        return new ServicoResumoDTO(s.getId(), s.getNome(), s.getPreco());
    }

    private static PecaResumoDTO toPecaResumo(Peca p) {
        return new PecaResumoDTO(p.getId(), p.getNome(), p.getPreco(), p.getQtdEstoque());
    }
}
