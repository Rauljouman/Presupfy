package com.presupuestos.presupuestosBackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class LineaPresupuestoRequestDTO {

    private Long productoServicioId; // puede ser null si es una línea manual

    private String nombre;
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal tipoIva;

    private BigDecimal porcentajeDescuento = BigDecimal.ZERO;

    public Long getProductoServicioId() {
        return productoServicioId;
    }

    public void setProductoServicioId(Long productoServicioId) {
        this.productoServicioId = productoServicioId;
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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(BigDecimal tipoIva) {
        this.tipoIva = tipoIva;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}

