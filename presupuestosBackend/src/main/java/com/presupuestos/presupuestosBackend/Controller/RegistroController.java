
package com.presupuestos.presupuestosBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO;
import com.presupuestos.presupuestosBackend.dto.RegistroEmpresaRequestDTO;
import com.presupuestos.presupuestosBackend.service.RegistroEmpresaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    private final RegistroEmpresaService registroEmpresaService;

    public RegistroController(RegistroEmpresaService registroEmpresaService) {
        this.registroEmpresaService = registroEmpresaService;
    }

    @PostMapping("/empresa")
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaResponseDTO registrarEmpresa(@Valid @RequestBody RegistroEmpresaRequestDTO dto,
                                               @AuthenticationPrincipal Jwt jwt){

        return registroEmpresaService.registrarEmpresa(dto, jwt);
    }
}
