package com.presupuestos.presupuestosBackend.mapper;

import com.presupuestos.presupuestosBackend.dto.ProductoServicioResponseDTO;
import com.presupuestos.presupuestosBackend.model.ProductoServicio;
import org.springframework.stereotype.Component;

@Component
public class ProductoServicioMapper {

    public ProductoServicioResponseDTO toResponseDTO(ProductoServicio producto) {

        ProductoServicioResponseDTO dto = new ProductoServicioResponseDTO();

        dto.setId(producto.getId());
        dto.setTipo(producto.getTipo());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecioBase(producto.getPrecioBase());
        dto.setTipoIva(producto.getTipoIva());
        dto.setActivo(producto.isActivo());
        dto.setEmpresaId(producto.getEmpresa().getId());

        return dto;
    }
}