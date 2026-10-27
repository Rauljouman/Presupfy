package com.presupuestos.presupuestosBackend.mapper;

import com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO;
import com.presupuestos.presupuestosBackend.model.Empresa;

import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public EmpresaResponseDTO toResponseDTO(Empresa empresa) {

        EmpresaResponseDTO dto = new EmpresaResponseDTO();

        dto.setId(empresa.getId());
        dto.setRazonSocial(empresa.getRazonSocial());
        dto.setNombreComercial(empresa.getNombreComercial());
        dto.setNif(empresa.getNif());
        dto.setDireccion(empresa.getDireccion());
        dto.setCodigoPostal(empresa.getCodigoPostal());
        dto.setCiudad(empresa.getCiudad());
        dto.setProvincia(empresa.getProvincia());
        dto.setPais(empresa.getPais());
        dto.setTelefono(empresa.getTelefono());
        dto.setEmail(empresa.getEmail());
        dto.setLogoUrl(empresa.getLogoUrl());
        dto.setActivo(empresa.isActivo());

        return dto;
    }
}