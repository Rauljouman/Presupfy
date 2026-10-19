package com.presupuestos.presupuestosBackend.mapper;

import org.springframework.stereotype.Component;

import com.presupuestos.presupuestosBackend.dto.InvitacionResponseDTO;
import com.presupuestos.presupuestosBackend.model.Invitacion;

@Component
public class InvitacionMapper {

    public InvitacionResponseDTO toResponseDTO(Invitacion invitacion){

        InvitacionResponseDTO dto = new InvitacionResponseDTO();

        dto.setId(invitacion.getId());
        dto.setEmail(invitacion.getEmail());
        dto.setRol(invitacion.getRol());
        dto.setFechaCreacion(invitacion.getFechaCreacion());
        dto.setFechaExpiracion(invitacion.getFechaExpiracion());
        dto.setAceptada(invitacion.isAceptada());

        if(invitacion.getEmpresa() != null){
            dto.setEmpresaId(invitacion.getEmpresa().getId());
        }

        return dto;
    }
}