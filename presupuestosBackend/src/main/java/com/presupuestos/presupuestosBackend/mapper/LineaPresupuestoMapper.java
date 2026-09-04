package com.presupuestos.presupuestosBackend.mapper;

import com.presupuestos.presupuestosBackend.dto.LineaPresupuestoResponseDTO;
import com.presupuestos.presupuestosBackend.model.LineaPresupuesto;
import org.springframework.stereotype.Component;

@Component
public class LineaPresupuestoMapper {

    public LineaPresupuestoResponseDTO toResponseDTO(LineaPresupuesto lineaPresupuesto){
        
        LineaPresupuestoResponseDTO dto = new LineaPresupuestoResponseDTO();

        dto.setId(lineaPresupuesto.getId());
        if (lineaPresupuesto.getProductoServicio() != null) {
            dto.setProductoServicioId(lineaPresupuesto.getProductoServicio().getId());
        }
        dto.setNombre(lineaPresupuesto.getNombre());
        dto.setDescripcion(lineaPresupuesto.getDescripcion());
        dto.setCantidad(lineaPresupuesto.getCantidad());
        dto.setPrecioUnitario(lineaPresupuesto.getPrecioUnitario());
        dto.setTipoIva(lineaPresupuesto.getTipoIva());
        dto.setPorcentajeDescuento(lineaPresupuesto.getPorcentajeDescuento());
        dto.setTotalLinea(lineaPresupuesto.calcularTotal());

        return dto;
    }
}
