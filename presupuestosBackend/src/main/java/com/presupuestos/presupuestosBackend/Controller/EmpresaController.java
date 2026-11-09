package com.presupuestos.presupuestosBackend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.presupuestos.presupuestosBackend.dto.EmpresaRequestDTO;
import com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO;
import com.presupuestos.presupuestosBackend.service.EmpresaService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    public List<EmpresaResponseDTO> listarEmpresa(@RequestParam Long usuarioId){
        return empresaService.listarEmpresa(usuarioId);
    }

    @GetMapping("/{id}")
    public EmpresaResponseDTO buscarEmpresaId(@PathVariable Long id, @RequestParam  Long usuarioId){
        return empresaService.buscarEmpresaId(id, usuarioId);
    } 

    @PostMapping 
    public EmpresaResponseDTO crearEmpresa(@Valid @RequestBody EmpresaRequestDTO dto,@RequestParam Long usuarioId){
        return empresaService.crearEmpresa(dto, usuarioId);
    }

    @PutMapping("/{id}")
    public EmpresaResponseDTO actualizarEmpresa(@PathVariable Long id,@Valid @RequestBody EmpresaRequestDTO dto, @RequestParam Long usuarioId){
        return empresaService.actualizarEmpresa(id, dto, usuarioId);
    }

    @DeleteMapping ("/{id}")
    public void eliminarEmpresa(@PathVariable Long id,@RequestParam Long usuarioId){
        empresaService.eliminarEmpresa(id, usuarioId);
    }

    @PutMapping ("/{id}/activar")
    public void reactivarEmpresa(@PathVariable Long id, @RequestParam Long usuarioId){
        empresaService.reactivarEmpresa(id, usuarioId);
    }

    @PutMapping ("/{id}/desactivar")
    public void desactivarEmpresa(@PathVariable Long id, @RequestParam Long usuarioId){
        empresaService.desactivarEmpresa(id, usuarioId);
    }
}