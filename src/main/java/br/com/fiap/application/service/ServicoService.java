package br.com.fiap.application.service;

import br.com.fiap.domain.model.Servico;
import br.com.fiap.domain.repository.ServicoRepository;
import br.com.fiap.presentation.dto.request.AtualizarServicoRequest;
import br.com.fiap.presentation.dto.request.CriarServicoRequest;
import br.com.fiap.presentation.dto.response.ServicoDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.exception.ValidationException;
import br.com.fiap.presentation.mapper.ServicoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public ServicoDTO cadastraServico(CriarServicoRequest request) {
        log.info("Cadastrando servico: {}", request.nome());
        if (request.preco() == null || request.preco().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("preco", "Preco deve ser maior que zero", "PRECO_INVALIDO");
        }
        var servico = new Servico(request.nome(), request.descricao(), request.preco());
        var salvo = servicoRepository.save(servico);
        log.info("Servico cadastrado com sucesso. id: {}", salvo.getId());
        return ServicoMapper.toDTO(salvo);
    }

    public Optional<Servico> getServicoById(Long id) {
        return servicoRepository.findById(id);
    }

    public ServicoDTO atualizaServico(Long id, AtualizarServicoRequest request) {
        log.info("Atualizando servico id: {}", id);
        var servico = servicoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Servico nao encontrado. id: {}", id);
                    return new ResourceNotFoundException("Servico", "id", id);
                });
        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setPreco(request.preco());
        var atualizado = servicoRepository.save(servico);
        log.info("Servico atualizado com sucesso. id: {}", id);
        return ServicoMapper.toDTO(atualizado);
    }

    public void deletaServico(Long id) {
        if (servicoRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Servico", "id", id);
        }
        servicoRepository.deleteById(id);
    }
}
