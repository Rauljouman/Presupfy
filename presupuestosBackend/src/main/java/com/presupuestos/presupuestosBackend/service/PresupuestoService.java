package com.presupuestos.presupuestosBackend.service;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.enums.EstadoPresupuesto;

import com.presupuestos.presupuestosBackend.model.Cliente;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.model.Presupuesto;

import com.presupuestos.presupuestosBackend.repository.ClienteRepository;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.PresupuestoRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    public PresupuestoService(UsuarioRepository usuarioRepository, PresupuestoRepository presupuestoRepository, ClienteRepository clienteRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.presupuestoRepository = presupuestoRepository;
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
    }

    public Presupuesto crearPresupuesto(Presupuesto presupuesto){
        
        Long clienteId = presupuesto.getCliente().getId();
        Optional<Cliente> clienteEncontrado = clienteRepository.findById(clienteId);

        if(clienteEncontrado.isEmpty()){
            throw new RuntimeException("Error, no existe el cliente"); 
        }

        Cliente cliente = clienteEncontrado.get();


        Long empresaId = presupuesto.getEmpresa().getId();
        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaId);

        if(empresaEncontrada.isEmpty()){
            throw new RuntimeException("Error, no existe la empresa"); 
        }

        Empresa empresa = empresaEncontrada.get();

        if(!cliente.getEmpresa().getId().equals(empresa.getId())) {
            throw new RuntimeException("Error, el cliente no pertenece a esta empresa"); 
        }

        Long usuarioId = presupuesto.getUsuario().getId();
        Optional<Usuario> usuarioEncontrado =  usuarioRepository.findById(usuarioId);

        if(usuarioEncontrado.isEmpty()){
            throw new RuntimeException("Error, usuario no encontrado");
        }

        Usuario usuario = usuarioEncontrado.get();

        if(!usuario.getEmpresa().getId().equals(empresa.getId())){
            throw new RuntimeException("Error, el usuario no pertenece a esta empresa");
        }   
        
        int anio = LocalDate.now().getYear();
        LocalDate fechaInicio = LocalDate.of(anio,1,1);
        LocalDate fechaFin = LocalDate.of(anio, 12,31);

        Optional<Presupuesto> ultimoPresupuesto = presupuestoRepository.findFirstByEmpresaAndFechaCreacionBetweenOrderByNumeroPresupuestoDesc(empresa, fechaInicio, fechaFin);

        int siguienteNumero;

        if(ultimoPresupuesto.isEmpty()){
            siguienteNumero = 1;

        } else {
            Presupuesto ultimo = ultimoPresupuesto.get();

            String numeroAnterior = ultimo.getNumeroPresupuesto();

            String[] partes = numeroAnterior.split("-");

            String numero = partes[2];

            int numeroConvertido = Integer.parseInt(numero);

            siguienteNumero = numeroConvertido + 1;

            
        }

        String numeroFormateado = String.format("%04d", siguienteNumero);
        String numeroPresupuesto = "PRE-" + anio + "-" + numeroFormateado;

        presupuesto.setUsuario(usuario);
        presupuesto.setCliente(cliente);
        presupuesto.setEstado(EstadoPresupuesto.BORRADOR);
        presupuesto.setFechaCreacion(LocalDate.now());
        presupuesto.setEmpresa(empresa);
        presupuesto.setNumeroPresupuesto(numeroPresupuesto);

        return presupuestoRepository.save(presupuesto);
    }

    public Presupuesto enviarPresupuesto(Long id){
        Optional<Presupuesto> presupuestoEncontrado = presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RuntimeException("No se ha encontrado el presupuesto");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if(presupuesto.getEstado() != EstadoPresupuesto.BORRADOR){
            throw new RuntimeException("El presupuesto solo se puede enviar en estado borrador");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        }

        presupuesto.setEstado(EstadoPresupuesto.ENVIADO);
        presupuesto.setFechaEnvio(LocalDate.now());;
        return presupuestoRepository.save(presupuesto);
    }

    public Presupuesto aprobarPresupuesto(Long id){
        Optional<Presupuesto> presupuestoEncontrado = presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RuntimeException("No se ha encontrado el presupuesto");
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if(presupuesto.getEstado() != EstadoPresupuesto.ENVIADO){
            throw new RuntimeException("No se ha enviado el presupuesto");
        }

        presupuesto.setEstado(EstadoPresupuesto.APROBADO);
        presupuesto.setFechaRespuesta(LocalDate.now());

        return presupuestoRepository.save(presupuesto);
    }

    public Presupuesto rechazarPresupuesto(Long id){
        Optional<Presupuesto> presupuestoEncontrado = presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RuntimeException("No se ha encontrado el presupuesto");
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if (presupuesto.getEstado() != EstadoPresupuesto.ENVIADO) {
            throw new RuntimeException("Solo se pueden rechazar presupuestos enviados");
        }

        presupuesto.setEstado(EstadoPresupuesto.RECHAZADO);
        presupuesto.setFechaRespuesta(LocalDate.now());

        return presupuestoRepository.save(presupuesto);
    }

    public Presupuesto caducarPresupuesto(Long id){
        Optional<Presupuesto> presupuestoEncontrado = presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RuntimeException("No se encontro el presupuesto");
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if(presupuesto.getEstado() != EstadoPresupuesto.ENVIADO){
            throw new RuntimeException("Solo se pueden caducar presupuestos enviados");
        }

        presupuesto.setEstado(EstadoPresupuesto.CADUCADO);
        return presupuestoRepository.save(presupuesto);
    }

    public boolean necesitaAvisoRespuesta(Presupuesto presupuesto){

        if(presupuesto.getEstado() != EstadoPresupuesto.ENVIADO){
            return false;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = presupuesto.getFechaEnvio().plusDays(15);

        if(hoy.isEqual(fechaLimite) || hoy.isAfter(fechaLimite)){
            return true;
        }
        
        return false;
    }

}