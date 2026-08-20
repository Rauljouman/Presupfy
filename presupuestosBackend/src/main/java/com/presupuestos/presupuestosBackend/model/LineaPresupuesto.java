package com.presupuestos.presupuestosBackend.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
public class LineaPresupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_servicio_id")
    private ProductoServicio productoServicio;

    private String nombre;

    private String descripcion;

    private BigDecimal cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal tipoIva;

    private BigDecimal porcentajeDescuento;

    @ManyToOne
    @JoinColumn(name = "presupuesto_id", nullable = false)
    private Presupuesto presupuesto;

    public LineaPresupuesto() {
    }

    public LineaPresupuesto(
            ProductoServicio productoServicio,
            String nombre,
            String descripcion,
            BigDecimal cantidad,
            BigDecimal precioUnitario,
            BigDecimal tipoIva,
            BigDecimal porcentajeDescuento,
            Presupuesto presupuesto) {

        this.productoServicio = productoServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoIva = tipoIva;
        this.porcentajeDescuento = porcentajeDescuento;
        this.presupuesto = presupuesto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductoServicio getProductoServicio() {
        return productoServicio;
    }

    public void setProductoServicio(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
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

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }
}