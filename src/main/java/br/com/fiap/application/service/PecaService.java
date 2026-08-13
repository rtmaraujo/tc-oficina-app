package br.com.fiap.application.service;

import br.com.fiap.domain.model.Peca;
import br.com.fiap.domain.repository.PecaRepository;
import br.com.fiap.presentation.dto.request.AtualizarPecaRequest;
import br.com.fiap.presentation.dto.request.CriarPecaRequest;
import br.com.fiap.presentation.dto.response.PecaDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.exception.ValidationException;
import br.com.fiap.presentation.mapper.PecaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class PecaService {

    private final PecaRepository pecaRepository;

    public PecaService(PecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    public PecaDTO cadastraPeca(CriarPecaRequest request) {
        log.info("Cadastrando peca: {}", request.nome());
        if (request.preco() == null || request.preco().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("preco", "Preco deve ser maior que zero", "PRECO_INVALIDO");
        }
        var peca = new Peca(request.nome(), request.descricao(), request.preco(), request.qtdEstoque());
        var salva = pecaRepository.save(peca);
        log.info("Peca cadastrada com sucesso. id: {}", salva.getId());
        return PecaMapper.toDTO(salva);
    }

    public Optional<Peca> getPecaById(Long id) {
        return pecaRepository.findById(id);
    }

    public PecaDTO atualizaPeca(Long id, AtualizarPecaRequest request) {
        log.info("Atualizando peca id: {}", id);
        var peca = pecaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Peca nao encontrada. id: {}", id);
                    return new ResourceNotFoundException("Peca", "id", id);
                });
        peca.setNome(request.nome());
        peca.setDescricao(request.descricao());
        peca.setPreco(request.preco());
        peca.setQtdEstoque(request.qtdEstoque());
        return PecaMapper.toDTO(pecaRepository.save(peca));
    }

    public void deletaPeca(Long id) {
        if (pecaRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Peca", "id", id);
        }
        pecaRepository.deleteById(id);
    }
}
