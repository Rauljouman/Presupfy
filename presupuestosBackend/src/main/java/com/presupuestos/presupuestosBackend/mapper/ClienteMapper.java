package com.presupuestos.presupuestosBackend.mapper;

import com.presupuestos.presupuestosBackend.dto.ClienteResponseDTO;
import com.presupuestos.presupuestosBackend.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {

        ClienteResponseDTO dto = new ClienteResponseDTO();

        dto.setId(cliente.getId());
        dto.setTipoCliente(cliente.getTipoCliente());

        dto.setNombre(cliente.getNombre());
        dto.setPrimerApellido(cliente.getPrimerApellido());
        dto.setSegundoApellido(cliente.getSegundoApellido());
        dto.setDni(cliente.getDni());

        dto.setRazonSocial(cliente.getRazonSocial());
        dto.setNif(cliente.getNif());

        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());

        dto.setEmpresaId(cliente.getEmpresa().getId());

        return dto;
    }
}