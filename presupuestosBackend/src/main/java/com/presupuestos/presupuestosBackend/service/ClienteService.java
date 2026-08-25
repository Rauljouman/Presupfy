package com.presupuestos.presupuestosBackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.model.Cliente;
import com.presupuestos.presupuestosBackend.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}