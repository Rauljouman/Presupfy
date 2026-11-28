
package com.presupuestos.presupuestosBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegistroEmpresaRequestDTO {

    @Valid
    @NotNull(message = "Los datos de la empresa son obligatorios")
    private EmpresaRequestDTO empresa;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    private String primerApellido;

    private String segundoApellido;

    private String telefono;

    // GETTERS Y SETTERS

    public EmpresaRequestDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaRequestDTO empresa) {
        this.empresa = empresa;
    }

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
}
