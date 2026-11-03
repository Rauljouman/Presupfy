package com.presupuestos.presupuestosBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDTO {

    @NotBlank
    private String nombre;

    private String primerApellido;

    private String segundoApellido;

    private String telefono;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String tokenInvitacion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenInvitacion() {
        return tokenInvitacion;
    }

    public void setTokenInvitacion(String tokenInvitacion) {
        this.tokenInvitacion = tokenInvitacion;
    }
}