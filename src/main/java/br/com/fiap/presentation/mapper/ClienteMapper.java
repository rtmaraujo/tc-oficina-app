package br.com.fiap.presentation.mapper;

import br.com.fiap.domain.model.Cliente;
import br.com.fiap.presentation.dto.response.ClienteDTO;
import br.com.fiap.presentation.dto.response.ClienteResumoDTO;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getCpfCnpj() != null ? cliente.getCpfCnpj().getValue() : null,
            cliente.getEmail(),
            cliente.getTelefone()
        );
    }

    public static ClienteResumoDTO toResumoDTO(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteResumoDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail()
        );
    }
}
