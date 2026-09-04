package com.presupuestos.presupuestosBackend.mapper;

import java.util.ArrayList;
import java.util.List;

import com.presupuestos.presupuestosBackend.dto.LineaPresupuestoResponseDTO;
import com.presupuestos.presupuestosBackend.model.LineaPresupuesto;
import com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO;
import com.presupuestos.presupuestosBackend.model.Presupuesto;
import org.springframework.stereotype.Component;

@Component
public class PresupuestoMapper {
    
    private final LineaPresupuestoMapper lineaPresupuestoMapper;

    public PresupuestoResponseDTO toResponseDTO(Presupuesto presupuesto){
        

        PresupuestoResponseDTO dto = new PresupuestoResponseDTO();

         dto.setId(presupuesto.getId());
         dto.setTitulo(presupuesto.getTitulo());
         dto.setNumeroPresupuesto(presupuesto.getNumeroPresupuesto());
         dto.setEstado(presupuesto.getEstado());
         dto.setFechaCreacion(presupuesto.getFechaCreacion());
         dto.setFechaEnvio(presupuesto.getFechaEnvio());
         dto.setFechaRespuesta(presupuesto.getFechaRespuesta());
         dto.setNotas(presupuesto.getNotas());

         if(presupuesto.getUsuario() != null){
            dto.setUsuarioId(presupuesto.getUsuario().getId());
         }

         if(presupuesto.getEmpresa() != null){
            dto.setEmpresaId(presupuesto.getEmpresa().getId());
         }

         if(presupuesto.getCliente() != null ){
            dto.setClienteId(presupuesto.getCliente().getId());
         }

         dto.setTotal(presupuesto.calcularTotal());

        List<LineaPresupuestoResponseDTO> lineasDTO = new ArrayList<>();

        for (LineaPresupuesto linea : presupuesto.getLineas()) {
            lineasDTO.add(lineaPresupuestoMapper.toResponseDTO(linea));
        }
        
        dto.setLineas(lineasDTO);

        return dto;
    }
    
    public PresupuestoMapper(LineaPresupuestoMapper lineaPresupuestoMapper) {
        this.lineaPresupuestoMapper = lineaPresupuestoMapper;
    }
}
