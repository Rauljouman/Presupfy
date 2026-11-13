package com.presupuestos.presupuestosBackend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.presupuestos.presupuestosBackend.dto.EmpresaRequestDTO;
import com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO;
import com.presupuestos.presupuestosBackend.service.EmpresaService;
import com.presupuestos.presupuestosBackend.service.UsuarioAutenticadoService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public EmpresaController(EmpresaService empresaService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.empresaService = empresaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping
    public List<EmpresaResponseDTO> listarEmpresa(){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        return empresaService.listarEmpresa(usuarioId);
    }

    @GetMapping("/{id}")
    public EmpresaResponseDTO buscarEmpresaId(@PathVariable Long id){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        return empresaService.buscarEmpresaId(id, usuarioId);
    } 

    @PostMapping 
    public EmpresaResponseDTO crearEmpresa(@Valid @RequestBody EmpresaRequestDTO dto){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();

        return empresaService.crearEmpresa(dto, usuarioId);
    }

    @PutMapping("/{id}")
    public EmpresaResponseDTO actualizarEmpresa(@PathVariable Long id,@Valid @RequestBody EmpresaRequestDTO dto){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        return empresaService.actualizarEmpresa(id, dto, usuarioId);
    }

    @DeleteMapping ("/{id}")
    public void eliminarEmpresa(@PathVariable Long id){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        empresaService.eliminarEmpresa(id, usuarioId);
    }

    @PutMapping ("/{id}/activar")
    public void reactivarEmpresa(@PathVariable Long id){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        empresaService.reactivarEmpresa(id, usuarioId);
    }

    @PutMapping ("/{id}/desactivar")
    public void desactivarEmpresa(@PathVariable Long id){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        empresaService.desactivarEmpresa(id, usuarioId);
    }
}