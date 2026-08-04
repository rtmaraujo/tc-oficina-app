package br.com.fiap.domain.service;

import br.com.fiap.domain.model.OrdemServico;
import br.com.fiap.domain.model.OrdemServicoStatus;
import br.com.fiap.presentation.exception.ValidationException;

public class OrdemServicoDomainService {

    public void avancaStatus(OrdemServico ordemServico) {
        if (ordemServico.isCompleto()) {
            throw new ValidationException("status", "Ordem ja esta ENTREGUE, nao e possivel avancar", "STATUS_INVALIDO");
        }
        switch (ordemServico.getStatus()) {
            case RECEBIDA:
                ordemServico.atualizaStatus(OrdemServicoStatus.EM_DIAGNOSTICO);
                break;
            case EM_DIAGNOSTICO:
                ordemServico.atualizaStatus(OrdemServicoStatus.AGUARDANDO_APROVACAO);
                break;
            case AGUARDANDO_APROVACAO:
                throw new ValidationException("status",
                    "Ordem aguardando aprovacao do cliente. Use /api/v1/publico/ordem/{id}/aprovar-orcamento para aprovacao/recusa",
                    "APPROVAL_REQUIRED");
            case EM_EXECUCAO:
                ordemServico.atualizaStatus(OrdemServicoStatus.FINALIZADA);
                break;
            case FINALIZADA:
                ordemServico.atualizaStatus(OrdemServicoStatus.ENTREGUE);
                break;
            default:
                break;
        }
    }

    public void aprovarOrcamento(OrdemServico ordemServico, boolean aprovado) {
        if (ordemServico.getStatus() != OrdemServicoStatus.AGUARDANDO_APROVACAO) {
            throw new ValidationException("status",
                "Ordem nao esta AGUARDANDO_APROVACAO. Status atual: " + ordemServico.getStatus(),
                "STATUS_INVALIDO");
        }
        if (aprovado) {
            ordemServico.atualizaStatus(OrdemServicoStatus.EM_EXECUCAO);
        } else {
            ordemServico.atualizaStatus(OrdemServicoStatus.RECUSADA);
        }
    }
}
