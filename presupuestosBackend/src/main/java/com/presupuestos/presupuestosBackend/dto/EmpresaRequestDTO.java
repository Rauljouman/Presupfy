package com.presupuestos.presupuestosBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EmpresaRequestDTO {

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 150)
    private String razonSocial;

    @Size(max = 150)
    private String nombreComercial;

    @NotBlank(message = "El NIF es obligatorio")
    @Pattern(
        regexp = "^[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]$",
        message = "El formato del NIF no es válido"
    )
    private String nif;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @Size(max = 100)
    private String provincia;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
        regexp = "^[+]?[0-9 ]{9,15}$",
        message = "El formato del teléfono no es válido"
    )
    private String telefono;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    @Size(max = 255)
    private String logoUrl;

    // GETTERS Y SETTERS

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}