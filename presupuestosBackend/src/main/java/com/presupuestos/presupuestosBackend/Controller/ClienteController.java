package com.presupuestos.presupuestosBackend.controller;

import java.util.List;

import com.presupuestos.presupuestosBackend.dto.ClienteRequestDTO;
import com.presupuestos.presupuestosBackend.dto.ClienteResponseDTO;
import com.presupuestos.presupuestosBackend.exception.SolicitudInvalidaException;
import com.presupuestos.presupuestosBackend.service.ClienteService;
import com.presupuestos.presupuestosBackend.service.UsuarioAutenticadoService;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public ClienteController(ClienteService clienteService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.clienteService = clienteService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping 
    public List<ClienteResponseDTO> listarCliente(){
        Long empresaId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getEmpresa().getId();
        return clienteService.listarCliente(empresaId);
    }

    @PostMapping
    public ClienteResponseDTO crearCliente(@Valid @RequestBody ClienteRequestDTO dto){

        if(dto.getTipoCliente() == null) {
            throw new SolicitudInvalidaException("El tipo de cliente es obligatorio"); 
        }

        Long empresaId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getEmpresa().getId();

        return clienteService.crearCliente(dto, empresaId);
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto){

        Long empresaId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getEmpresa().getId();

        return clienteService.actualizarCliente(id, dto, empresaId);
    }

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id){
        Long empresaId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getEmpresa().getId();
        clienteService.eliminarCliente(id, empresaId);
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscarCliente(@PathVariable Long id){
        Long empresaId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getEmpresa().getId();
        return clienteService.buscarCliente(id, empresaId);
    }
}
