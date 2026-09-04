package com.presupuestos.presupuestosBackend.dto;

import com.presupuestos.presupuestosBackend.enums.EstadoPresupuesto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PresupuestoResponseDTO {

    private Long id;

    private String titulo;
    private String numeroPresupuesto;
    private EstadoPresupuesto estado;

    private LocalDate fechaCreacion;
    private LocalDate fechaEnvio;
    private LocalDate fechaRespuesta;

    private String notas;

    private Long usuarioId;
    private Long empresaId;
    private Long clienteId;

    private List<LineaPresupuestoResponseDTO> lineas;

    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNumeroPresupuesto() {
        return numeroPresupuesto;
    }

    public void setNumeroPresupuesto(String numeroPresupuesto) {
        this.numeroPresupuesto = numeroPresupuesto;
    }

    public EstadoPresupuesto getEstado() {
        return estado;
    }

    public void setEstado(EstadoPresupuesto estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDate fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDate getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDate fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<LineaPresupuestoResponseDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPresupuestoResponseDTO> lineas) {
        this.lineas = lineas;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}