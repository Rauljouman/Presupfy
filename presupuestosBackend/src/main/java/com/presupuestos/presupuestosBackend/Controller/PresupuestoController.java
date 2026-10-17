package com.presupuestos.presupuestosBackend.Controller;

import java.util.List;

import com.presupuestos.presupuestosBackend.dto.PresupuestoRequestDTO;
import com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO;
import com.presupuestos.presupuestosBackend.service.PresupuestoService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presupuestos")
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    @GetMapping
    public List<PresupuestoResponseDTO> listarPresupuesto(@RequestParam Long empresaId){
        return presupuestoService.listarPresupuesto(empresaId);
    }

    @PostMapping 
    public PresupuestoResponseDTO crearPresupuesto(@Valid @RequestBody PresupuestoRequestDTO dto){
        return presupuestoService.crearPresupuesto(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresupuestoResponseDTO> buscarPresupuesto(@PathVariable Long id, @RequestParam Long empresaId) {
        PresupuestoResponseDTO presupuesto = presupuestoService.buscarPresupuesto(id, empresaId);
        return ResponseEntity.ok(presupuesto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresupuestoResponseDTO> modificarPresupuesto(@PathVariable Long id,@Valid  @RequestBody PresupuestoRequestDTO dto) {
        PresupuestoResponseDTO presupuesto = presupuestoService.modificarPresupuesto(id, dto);
        return ResponseEntity.ok(presupuesto);
    }

    @PatchMapping ("/{id}/enviar")
    public PresupuestoResponseDTO enviarPresupuesto(@PathVariable Long id, @RequestParam Long empresaId){
        return presupuestoService.enviarPresupuesto(id, empresaId);
    }

    @PatchMapping ("/{id}/aprobar")
    public PresupuestoResponseDTO aprobarPresupuesto(@PathVariable Long id, @RequestParam Long empresaId){
        return presupuestoService.aprobarPresupuesto(id, empresaId);
    }

    @PatchMapping ("/{id}/caducar")
    public PresupuestoResponseDTO caducarPresupuesto(@PathVariable Long id, @RequestParam Long empresaId){
        return presupuestoService.caducarPresupuesto(id, empresaId);
    }

    @PatchMapping("/{id}/rechazar")
    public PresupuestoResponseDTO rechazarPresupuesto(@PathVariable Long id, @RequestParam Long empresaId) {
        return presupuestoService.rechazarPresupuesto(id, empresaId);
    }

    @DeleteMapping("/{id}")
    public void eliminarPresupuesto(@PathVariable Long id, @RequestParam Long empresaId) {
        presupuestoService.eliminarPresupuesto(id, empresaId);
    }

}