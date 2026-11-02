package com.presupuestos.presupuestosBackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.presupuestos.presupuestosBackend.dto.InvitacionRequestDTO;
import com.presupuestos.presupuestosBackend.dto.InvitacionResponseDTO;
import com.presupuestos.presupuestosBackend.dto.UsuarioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.UsuarioResponseDTO;
import com.presupuestos.presupuestosBackend.service.InvitacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/invitaciones")
public class InvitacionController {

    private final InvitacionService invitacionService;

    public InvitacionController(InvitacionService invitacionService) {
        this.invitacionService = invitacionService;
    }

    @PostMapping
    public InvitacionResponseDTO crearInvitacion(
            @Valid @RequestBody InvitacionRequestDTO dto,
            @RequestParam Long usuarioId) {

        return invitacionService.crearInvitacion(dto, usuarioId);
    }

    @GetMapping
    public List<InvitacionResponseDTO> listarInvitaciones(
            @RequestParam Long usuarioId) {

        return invitacionService.listarInvitaciones(usuarioId);
    }

    @PostMapping("/{id}/reenviar")
    public InvitacionResponseDTO reenviarInvitacion(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        return invitacionService.reenviarInvitacion(id, usuarioId);
    }

    @PostMapping("/aceptar")
    public UsuarioResponseDTO aceptarInvitacion(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        return invitacionService.aceptarInvitacion(dto);
    }
}