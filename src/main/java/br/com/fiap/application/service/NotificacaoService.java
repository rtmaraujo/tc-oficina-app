package br.com.fiap.application.service;

import br.com.fiap.domain.model.OrdemServico;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificacaoService {

    public void notificarAprovacaoOrcamento(OrdemServico ordem, boolean aprovado, String observacoes) {
        var clienteNome = ordem.getCliente().getNome();
        var osId = ordem.getId();
        if (aprovado) {
            log.info("Para: {}", ordem.getCliente().getEmail());
            log.info("Assunto: Orcamento Aprovado - OS #{}", osId);
            log.info("Mensagem: Ola {}, seu orcamento para OS #{} foi APROVADO. Sua ordem entrou em execucao.",
                    clienteNome, osId);
            if (observacoes != null && !observacoes.isBlank()) {
                log.info("Observacoes do cliente: {}", observacoes);
            }
            log.info("====================");
        } else {
            log.info("Para: {}", ordem.getCliente().getEmail());
            log.info("Assunto: Orcamento Recusado - OS #{}", osId);
            log.info("Mensagem: Ola {}, seu orcamento para OS #{} foi RECUSADO. Sua ordem permanece em analise.",
                    clienteNome, osId);
            if (observacoes != null && !observacoes.isBlank()) {
                log.info("Observacoes do cliente: {}", observacoes);
            }
            log.info("====================");
        }
    }

    public void notificarAtualizacaoStatus(OrdemServico ordem, String statusAnterior, String statusNovo) {
        log.info("Para: {}", ordem.getCliente().getEmail());
        log.info("Assunto: Atualizacao de Status - OS #{}", ordem.getId());
        log.info("Mensagem: Ola {}, o status da sua OS #{} mudou de {} para {}.",
                ordem.getCliente().getNome(),
                ordem.getId(),
                statusAnterior,
                statusNovo);
        log.info("====================");
    }

    public void notificarOrdemCriada(OrdemServico ordem) {
        log.info("Para: {}", ordem.getCliente().getEmail());
        log.info("Assunto: Ordem de Servico Criada - OS #{}", ordem.getId());
        log.info("Mensagem: Ola {}, sua ordem de servico #{} foi criada com sucesso. Status: {}.",
                ordem.getCliente().getNome(),
                ordem.getId(),
                ordem.getStatus());
        log.info("====================");
    }
}
