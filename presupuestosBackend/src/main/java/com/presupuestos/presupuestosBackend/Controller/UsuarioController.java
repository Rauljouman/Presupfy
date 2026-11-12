
package com.presupuestos.presupuestosBackend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.presupuestos.presupuestosBackend.dto.CambiarRolRequestDTO;
import com.presupuestos.presupuestosBackend.dto.UsuarioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.UsuarioResponseDTO;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.service.UsuarioService;
import com.presupuestos.presupuestosBackend.service.UsuarioAutenticadoService;
import com.presupuestos.presupuestosBackend.mapper.UsuarioMapper;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioAutenticadoService usuarioAutenticadoService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping("/me")
    public UsuarioResponseDTO obtenerMiPerfil(){
        Usuario usuario = usuarioAutenticadoService.obtenerUsuarioAutenticado();
        return usuarioMapper.toResponseDTO(usuario);
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuario(@RequestParam Long empresaId){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        return usuarioService.listarUsuario(empresaId, usuarioId);
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscarUsuarioId(@PathVariable Long id, @RequestParam Long usuarioId){
        return usuarioService.buscarUsuarioId(id, usuarioId);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto, @RequestParam Long usuarioId){
        return usuarioService.actualizarUsuario(id, dto, usuarioId);
    }

    @PatchMapping("/{id}/rol")
    public UsuarioResponseDTO cambiarRolUsuario(@PathVariable Long id, @RequestBody CambiarRolRequestDTO dto, @RequestParam Long usuarioId){
        return usuarioService.cambiarRolUsuario(id, dto.getRol(), usuarioId);
    }
}
