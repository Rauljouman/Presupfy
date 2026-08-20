package com.presupuestos.presupuestosBackend.model;

import java.math.BigDecimal;

import com.presupuestos.presupuestosBackend.enums.TipoProductoServicio;

import jakarta.persistence.*;

@Entity
public class ProductoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoProductoServicio tipo;

    private String nombre;

    private String descripcion;

    private BigDecimal precioBase;

    private BigDecimal tipoIva;

    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    public ProductoServicio() {
    }

    public ProductoServicio(
            TipoProductoServicio tipo,
            String nombre,
            String descripcion,
            BigDecimal precioBase,
            BigDecimal tipoIva,
            Empresa empresa) {

        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.tipoIva = tipoIva;
        this.empresa = empresa;
    }

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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}