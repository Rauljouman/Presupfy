package com.presupuestos.presupuestosBackend.Controller;

import java.util.List;

import com.presupuestos.presupuestosBackend.dto.ClienteRequestDTO;
import com.presupuestos.presupuestosBackend.dto.ClienteResponseDTO;
import com.presupuestos.presupuestosBackend.service.ClienteService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    public List<ClienteResponseDTO> listarCliente(@RequestParam Long clienteId){
        return clienteService.listarCliente(clienteId);
    }

    


}
