package com.presupuestos.presupuestosBackend.service;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.model.Cliente;

import com.presupuestos.presupuestosBackend.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarCliente(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarClienteId(Long id){
        return clienteRepository.findById(id);
    }

    public Cliente crearCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public void elimianCliente(Long id){
        clienteRepository.deleteById(id);
    }

    public Cliente actualizarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }
}