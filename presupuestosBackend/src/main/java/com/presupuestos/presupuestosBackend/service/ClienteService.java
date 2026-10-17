package com.presupuestos.presupuestosBackend.service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.exception.SolicitudInvalidaException;
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

    public List<ClienteResponseDTO> listarCliente(Long empresaId){

        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaId);

        if(empresaEncontrada.isEmpty()){
            throw new RecursoNoEncontradoException("No se ha encontrado la empresa.");
        }

        Empresa empresa = empresaEncontrada.get();

        List<Cliente> clientes = clienteRepository.findByEmpresa(empresa); 

        List<ClienteResponseDTO> clienteDTO = new ArrayList<>();

        for(Cliente cliente : clientes){
            clienteDTO.add(clienteMapper.toResponseDTO(cliente));
        }

        return clienteDTO;
    }

    public ClienteResponseDTO buscarCliente(Long id, Long empresaId){
        
        Cliente cliente = clienteRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException("No se ha encontrado el cliente"));

        Empresa empresa = empresaRepository.findById(empresaId).orElseThrow(()-> new RecursoNoEncontradoException("No se ha podido encontrar la empresa"));

        if(!cliente.getEmpresa().getId().equals(empresa.getId())){
            throw new AccesoDenegadoException("El cliente no pertenece a esta empresa");
        }

        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO crearCliente(ClienteRequestDTO dto) {

        if(dto.getTipoCliente() == TipoCliente.PARTICULAR) {
            if(dto.getNombre() == null || dto.getNombre().isBlank() || dto.getDni() == null || dto.getDni().isBlank()){
                throw new SolicitudInvalidaException("El nombre y el DNI son obligatorios para un cliente particular");    
            }
        }else if (dto.getTipoCliente() == TipoCliente.EMPRESA){
            if(dto.getRazonSocial() == null || dto.getRazonSocial().isBlank() || dto.getNif() == null || dto.getNif().isBlank()){
                throw new SolicitudInvalidaException("La razón social y el NIF son obligatorios para un cliente empresa");    
            }
        }
        
        Long empresaId = dto.getEmpresaId();

        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaId);

        if(empresaEncontrada.isEmpty()){
            throw new RecursoNoEncontradoException("Error, no se ha encotrado la empresa");
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

    public void eliminarCliente(Long id, Long empresaId){
        
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encotrado al cliente"));

        Empresa empresa = empresaRepository.findById(empresaId).orElseThrow(()-> new RecursoNoEncontradoException("No se ha encontrado la empresa"));

        if(!cliente.getEmpresa().getId().equals(empresa.getId())){
            throw new AccesoDenegadoException("El cliente no pertenece a esta empresa");
        }

        clienteRepository.delete(cliente);
    }

    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(id).orElseThrow(() ->new RecursoNoEncontradoException( "No se ha encontrado el cliente"));

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId()).orElseThrow(() ->new RecursoNoEncontradoException("No se ha encontrado la empresa"));

        if (!cliente.getEmpresa().getId().equals(empresa.getId())) {throw new AccesoDenegadoException("El cliente no pertenece a esta empresa");}

        if (dto.getTipoCliente() == TipoCliente.PARTICULAR) {

            if (dto.getNombre() == null || dto.getNombre().isBlank()|| dto.getDni() == null || dto.getDni().isBlank()) {

                throw new SolicitudInvalidaException("El nombre y el DNI son obligatorios para un cliente particular");
            }

            cliente.setNombre(dto.getNombre());
            cliente.setPrimerApellido(dto.getPrimerApellido());
            cliente.setSegundoApellido(dto.getSegundoApellido());
            cliente.setDni(dto.getDni());

            cliente.setRazonSocial(null);
            cliente.setNif(null);

        } else if (dto.getTipoCliente() == TipoCliente.EMPRESA) {

            if (dto.getRazonSocial() == null || dto.getRazonSocial().isBlank()|| dto.getNif() == null || dto.getNif().isBlank()) {

                throw new SolicitudInvalidaException( "La razón social y el NIF son obligatorios para un cliente empresa");
            }

            cliente.setRazonSocial(dto.getRazonSocial());
            cliente.setNif(dto.getNif());

            cliente.setNombre(null);
            cliente.setPrimerApellido(null);
            cliente.setSegundoApellido(null);
            cliente.setDni(null);

        } else {
            throw new SolicitudInvalidaException("El tipo de cliente es obligatorio");
        }

        cliente.setTipoCliente(dto.getTipoCliente());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return clienteMapper.toResponseDTO(clienteActualizado);
    }
}