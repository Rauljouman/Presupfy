package com.presupuestos.presupuestosBackend.model;

import java.time.LocalDateTime;

import com.presupuestos.presupuestosBackend.enums.EstadoInvitacion;
import com.presupuestos.presupuestosBackend.enums.TipoRol;

import jakarta.persistence.*;

@Entity
public class Invitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false) 
    private String email;

    @Column (nullable = false)  
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRol rol;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;


    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private LocalDateTime fechaUltimoEnvio;

    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private EstadoInvitacion estadoInvitacion;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getEmail(){
        return email;
    }

    public EstadoInvitacion getEstadoInvitacion(){
        return estadoInvitacion;
    }

    public void setEstadoInvitacion(EstadoInvitacion estadoInvitacion){
        this.estadoInvitacion = estadoInvitacion;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public TipoRol getRol(){
        return rol;
    }

    public void setRol(TipoRol rol){
        this.rol = rol;
    }

    public Empresa getEmpresa(){
        return empresa;
    }

    public void setEmpresa(Empresa empresa){
        this.empresa = empresa;
    }

    public LocalDateTime getFechaCreacion(){
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion){
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaExpiracion(){
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion){
        this.fechaExpiracion = fechaExpiracion;
    }

    public LocalDateTime getFechaUltimoEnvio(){
        return fechaUltimoEnvio;
    }

    public void setFechaUltimoEnvio(LocalDateTime fechaUltimoEnvio){
        this.fechaUltimoEnvio = fechaUltimoEnvio;
    }
}