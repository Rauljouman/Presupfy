package com.presupuestos.presupuestosBackend.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.presupuestos.presupuestosBackend.enums.EstadoPresupuesto;

import jakarta.persistence.*;

@Entity
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String numeroPresupuesto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPresupuesto estado = EstadoPresupuesto.BORRADOR;

    private LocalDate fechaCreacion;

    private LocalDate fechaEnvio;

    private LocalDate fechaRespuesta;

    private String notas;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(
            mappedBy = "presupuesto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<LineaPresupuesto> lineas = new ArrayList<>();

    public Presupuesto() {
    }

    public Presupuesto(
            String titulo,
            String numeroPresupuesto,
            EstadoPresupuesto estado,
            LocalDate fechaCreacion,
            LocalDate fechaEnvio,
            LocalDate fechaRespuesta,
            String notas,
            Usuario usuario,
            Empresa empresa,
            Cliente cliente) {

        this.titulo = titulo;
        this.numeroPresupuesto = numeroPresupuesto;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEnvio = fechaEnvio;
        this.fechaRespuesta = fechaRespuesta;
        this.notas = notas;
        this.usuario = usuario;
        this.empresa = empresa;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<LineaPresupuesto> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPresupuesto> lineas) {
        this.lineas = lineas;
    }
}