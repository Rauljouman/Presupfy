package com.presupuestos.presupuestosBackend.controller;

import java.util.List;

import com.presupuestos.presupuestosBackend.dto.PresupuestoRequestDTO;
import com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.service.PresupuestoService;
import com.presupuestos.presupuestosBackend.service.UsuarioAutenticadoService;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presupuestos")
public class PresupuestoController {

    private final PresupuestoService presupuestoService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public PresupuestoController(PresupuestoService presupuestoService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.presupuestoService = presupuestoService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping
    public List<PresupuestoResponseDTO> listarPresupuesto(){
        Long empresaId = usuarioAutenticadoService.obtenerEmpresaIdAutenticada();
        return presupuestoService.listarPresupuesto(empresaId);
    }

    @PostMapping 
    public PresupuestoResponseDTO crearPresupuesto(@Valid @RequestBody PresupuestoRequestDTO dto){
        Usuario usuario = usuarioAutenticadoService.obtenerUsuarioAutenticado();
        return presupuestoService.crearPresupuesto(dto, usuario.getId(), usuario.getEmpresa().getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresupuestoResponseDTO> buscarPresupuesto(@PathVariable Long id) {
        Long empresaId = usuarioAutenticadoService.obtenerEmpresaIdAutenticada();
        PresupuestoResponseDTO presupuesto = presupuestoService.buscarPresupuesto(id, empresaId);
        return ResponseEntity.ok(presupuesto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresupuestoResponseDTO> modificarPresupuesto(@PathVariable Long id,@Valid  @RequestBody PresupuestoRequestDTO dto) {
        
        Long empresaId = usuarioAutenticadoService.obtenerEmpresaIdAutenticada();

        PresupuestoResponseDTO presupuesto = presupuestoService.modificarPresupuesto(id, dto, empresaId);

        return ResponseEntity.ok(presupuesto);
    }

    @PatchMapping ("/{id}/enviar")
    public PresupuestoResponseDTO enviarPresupuesto(@PathVariable Long id){
        Long empresaId = usuarioAutenticadoService.obtenerEmpresaIdAutenticada();
        return presupuestoService.enviarPresupuesto(id, empresaId);
    }

    @PatchMapping ("/{id}/aprobar")
    public PresupuestoResponseDTO aprobarPresupuesto(@PathVariable Long id ){
        Long empresaId = usuarioAutenticadoService.obtenerEmpresaIdAutenticada();
        return presupuestoService.aprobarPresupuesto(id, empresaId);
    }

    @PatchMapping ("/{id}/caducar")
    public PresupuestoResponseDTO caducarPresupuesto(@PathVariable Long id){
        Long empresaId = usuarioAutenticadoService.obtenerEmpresaIdAutenticada();
        return presupuestoService.caducarPresupuesto(id, empresaId);
    }

    @PatchMapping("/{id}/rechazar")
    public PresupuestoResponseDTO rechazarPresupuesto(@PathVariable Long id ) {
        Long empresaId = usuarioAutenticadoService.obtenerEmpresaIdAutenticada();
        return presupuestoService.rechazarPresupuesto(id, empresaId);
    }

    @DeleteMapping("/{id}")
    public void eliminarPresupuesto(@PathVariable Long id) {
        Long empresaId = usuarioAutenticadoService.obtenerEmpresaIdAutenticada();
        presupuestoService.eliminarPresupuesto(id, empresaId);
    }

}