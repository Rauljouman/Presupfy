package com.presupuestos.presupuestosBackend.dto;

import com.presupuestos.presupuestosBackend.enums.TipoRol;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InvitacionRequestDTO {

    @NotBlank
    @Email
    private String email;

    @NotNull
    private TipoRol rol;

    @NotNull
    private Long empresaId;

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
}