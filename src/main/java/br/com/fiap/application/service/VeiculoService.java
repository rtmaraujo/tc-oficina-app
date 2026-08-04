package br.com.fiap.application.service;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.domain.model.Placa;
import br.com.fiap.domain.model.Veiculo;
import br.com.fiap.domain.repository.ClienteRepository;
import br.com.fiap.domain.repository.VeiculoRepository;
import br.com.fiap.presentation.dto.request.AtualizarVeiculoRequest;
import br.com.fiap.presentation.dto.request.CriarVeiculoRequest;
import br.com.fiap.presentation.dto.response.VeiculoDTO;
import br.com.fiap.presentation.exception.ResourceNotFoundException;
import br.com.fiap.presentation.exception.ValidationException;
import br.com.fiap.presentation.mapper.VeiculoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    public VeiculoService(VeiculoRepository veiculoRepository, ClienteRepository clienteRepository) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    public VeiculoDTO cadastraVeiculo(CriarVeiculoRequest request) {
        log.info("Cadastrando veiculo. placa: {}, clienteId: {}", request.placa(), request.clienteId());
        var cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> {
                    log.warn("Cliente nao encontrado. id: {}", request.clienteId());
                    return new ResourceNotFoundException("Cliente", "id", request.clienteId());
                });
        var placa = new Placa(request.placa());
        if (veiculoRepository.findByPlaca(placa).isPresent()) {
            log.warn("Placa ja cadastrada: {}", request.placa());
            throw new ValidationException("placa", "Ja existe veiculo com esta placa.", "PLACA_DUPLICADA");
        }
        var veiculo = new Veiculo(placa, request.marca(), request.modelo(), request.ano(), cliente);
        var salvo = veiculoRepository.save(veiculo);
        log.info("Veiculo cadastrado com sucesso. id: {}", salvo.getId());
        return VeiculoMapper.toDTO(salvo);
    }

    public Optional<Veiculo> getVeiculoById(Long id) {
        return veiculoRepository.findById(id);
    }

    public List<VeiculoDTO> getVeiculosByClienteId(Long clienteId) {
        return veiculoRepository.findByClienteId(clienteId).stream()
                .map(VeiculoMapper::toDTO)
                .toList();
    }

    public VeiculoDTO atualizaVeiculo(Long id, AtualizarVeiculoRequest request) {
        log.info("Atualizando veiculo id: {}", id);
        var veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Veiculo nao encontrado. id: {}", id);
                    return new ResourceNotFoundException("Veiculo", "id", id);
                });
        veiculo.setMarca(request.marca());
        veiculo.setModelo(request.modelo());
        veiculo.setAno(request.ano());
        var atualizado = veiculoRepository.save(veiculo);
        log.info("Veiculo atualizado com sucesso. id: {}", id);
        return VeiculoMapper.toDTO(atualizado);
    }

    public void deletaVeiculo(Long id) {
        if (veiculoRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Veiculo", "id", id);
        }
        veiculoRepository.deleteById(id);
    }
}
