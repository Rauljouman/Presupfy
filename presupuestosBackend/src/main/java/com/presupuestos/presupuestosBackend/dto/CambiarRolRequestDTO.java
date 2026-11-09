package com.presupuestos.presupuestosBackend.dto;

import com.presupuestos.presupuestosBackend.enums.TipoRol;
import jakarta.validation.constraints.NotNull;

public class CambiarRolRequestDTO {

    @NotNull(message = "El rol es obligatorio")
    private TipoRol rol;

    public TipoRol getRol() {
        return rol;
    }

    public void setRol(TipoRol rol) {
        this.rol = rol;
    }
}