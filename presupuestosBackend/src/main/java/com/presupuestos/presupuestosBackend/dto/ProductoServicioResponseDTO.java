package com.presupuestos.presupuestosBackend.dto;

import com.presupuestos.presupuestosBackend.enums.TipoProductoServicio;

import java.math.BigDecimal;

public class ProductoServicioResponseDTO {

    private Long id;
    private TipoProductoServicio tipo;
    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private BigDecimal tipoIva;
    private boolean activo;
    private Long empresaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoProductoServicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoProductoServicio tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public BigDecimal getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(BigDecimal tipoIva) {
        this.tipoIva = tipoIva;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
}