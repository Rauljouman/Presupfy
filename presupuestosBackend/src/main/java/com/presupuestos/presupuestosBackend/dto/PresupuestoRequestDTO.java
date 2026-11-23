package com.presupuestos.presupuestosBackend.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PresupuestoRequestDTO {

    private String titulo;
    
    private String notas;

    @NotNull
    private Long clienteId;

    private List<LineaPresupuestoRequestDTO> lineas;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<LineaPresupuestoRequestDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPresupuestoRequestDTO> lineas) {
        this.lineas = lineas;
    }
}