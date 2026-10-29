package com.presupuestos.presupuestosBackend.dto;

import java.time.LocalDateTime;

import com.presupuestos.presupuestosBackend.enums.EstadoInvitacion;
import com.presupuestos.presupuestosBackend.enums.TipoRol;

public class InvitacionResponseDTO {

    private Long id;
    private String email;
    private TipoRol rol;
    private Long empresaId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaExpiracion;
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

    public void setEmail(String email){
        this.email = email;
    }

    public TipoRol getRol(){
        return rol;
    }

    public void setRol(TipoRol rol){
        this.rol = rol;
    }

    public Long getEmpresaId(){
        return empresaId;
    }

    public void setEmpresaId(Long empresaId){
        this.empresaId = empresaId;
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

    public EstadoInvitacion EstadoInvitacion(){
        return estadoInvitacion;
    }

    public void setEstadoInvitacion(EstadoInvitacion estadoInvitacion){
        this.estadoInvitacion = estadoInvitacion;
    }
}