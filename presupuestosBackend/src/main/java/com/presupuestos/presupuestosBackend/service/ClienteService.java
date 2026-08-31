package com.presupuestos.presupuestosBackend.service;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.dto.ClienteRequestDTO;
import com.presupuestos.presupuestosBackend.dto.ClienteResponseDTO;
import com.presupuestos.presupuestosBackend.enums.TipoCliente;
import com.presupuestos.presupuestosBackend.mapper.ClienteMapper;
import com.presupuestos.presupuestosBackend.model.Cliente;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.repository.ClienteRepository;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, EmpresaRepository empresaRepository,ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.clienteMapper = clienteMapper;
    }

    public List<Cliente> listarCliente(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarClienteId(Long id){
        return clienteRepository.findById(id);
    }

    public ClienteResponseDTO crearCliente(ClienteRequestDTO dto) {

        if(dto.getTipoCliente() == TipoCliente.PARTICULAR) {
            if(dto.getNombre() == null || dto.getNombre().isBlank() || dto.getDni() == null || dto.getDni().isBlank()){
                throw new RuntimeException("El nombre y el DNI son obligatorios para un cliente particular");    
            }
        }else if (dto.getTipoCliente() == TipoCliente.EMPRESA){
            if(dto.getRazonSocial() == null || dto.getRazonSocial().isBlank() || dto.getNif() == null || dto.getNif().isBlank()){
                throw new RuntimeException("La razón social y el NIF son obligatorios para un cliente empresa");    
            }
        }
        
        Long empresaId = dto.getEmpresaId();

        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaId);

        if(empresaEncontrada.isEmpty()){
            throw new RuntimeException("Error, no se ha encotrado la empresa");
        }

        Empresa empresa = empresaEncontrada.get();
        Cliente cliente = new Cliente();

        if(dto.getTipoCliente() == TipoCliente.PARTICULAR){
            cliente.setNombre(dto.getNombre());
            cliente.setPrimerApellido(dto.getPrimerApellido());
            cliente.setSegundoApellido(dto.getSegundoApellido());
            cliente.setDni(dto.getDni());

        } else if (dto.getTipoCliente() == TipoCliente.EMPRESA) {
            cliente.setRazonSocial(dto.getRazonSocial());
            cliente.setNif(dto.getNif());
        }       

        cliente.setTipoCliente(dto.getTipoCliente());
            cliente.setDireccion(dto.getDireccion());
            cliente.setTelefono(dto.getTelefono());
            cliente.setEmail(dto.getEmail());
            cliente.setEmpresa(empresa);

        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(clienteGuardado);
    }

    public void elimianCliente(Long id){
        clienteRepository.deleteById(id);
    }

    public Cliente actualizarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }
}