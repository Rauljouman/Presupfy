package com.presupuestos.presupuestosBackend.Controller;

import com.presupuestos.presupuestosBackend.dto.PresupuestoRequestDTO;
import com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO;
import com.presupuestos.presupuestosBackend.service.PresupuestoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presupuestos")
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    @PostMapping 
    public PresupuestoResponseDTO crearPresupuesto(@Valid @RequestBody PresupuestoRequestDTO dto){
        return presupuestoService.crearPresupuesto(dto);
    }

    @PatchMapping ("/{id}/enviar")
    public PresupuestoResponseDTO enviarPresupuesto(@PathVariable Long id ){
        return presupuestoService.enviarPresupuesto(id);
    }

    @PatchMapping ("/{id}/aprobar")
    public PresupuestoResponseDTO aprobarPresupuesto(@PathVariable Long id){
        return presupuestoService.aprobarPresupuesto(id);
    }

    @PatchMapping ("/{id}/caducado")
    public PresupuestoResponseDTO caducarPresupuesto(@PathVariable Long id){
        return presupuestoService.caducarPresupuesto(id);
    }

    @PatchMapping("/{id}/rechazar")
    public PresupuestoResponseDTO rechazarPresupuesto(@PathVariable Long id) {
        return presupuestoService.rechazarPresupuesto(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarPresupuesto(@PathVariable Long id) {
        presupuestoService.eliminarPresupuesto(id);
    }

}