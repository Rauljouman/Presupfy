package com.presupuestos.presupuestosBackend.Controller;

import java.util.List;

import com.presupuestos.presupuestosBackend.dto.ClienteRequestDTO;
import com.presupuestos.presupuestosBackend.dto.ClienteResponseDTO;
import com.presupuestos.presupuestosBackend.exception.SolicitudInvalidaException;
import com.presupuestos.presupuestosBackend.service.ClienteService;

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

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping 
    public List<ClienteResponseDTO> listarCliente(@RequestParam Long empresId){
        return clienteService.listarCliente(empresId);
    }

    @PostMapping
    public ClienteResponseDTO crearCliente(@Valid @RequestBody ClienteRequestDTO dto){

        if(dto.getTipoCliente() == null) {
            throw new SolicitudInvalidaException("El tipo de cliente es obligatorio"); 
        }

        return clienteService.crearCliente(dto);
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto){
        return clienteService.actualizarCliente(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id,@RequestParam Long empresId){
        clienteService.eliminarCliente(id, empresId);
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscarCliente(@PathVariable Long id, @RequestParam Long empresaId){
        return clienteService.buscarCliente(id, empresaId);
    }
}
