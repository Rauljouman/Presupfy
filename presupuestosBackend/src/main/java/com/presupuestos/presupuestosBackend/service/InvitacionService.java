package com.presupuestos.presupuestosBackend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.presupuestos.presupuestosBackend.dto.InvitacionRequestDTO;
import com.presupuestos.presupuestosBackend.dto.InvitacionResponseDTO;
import com.presupuestos.presupuestosBackend.enums.EstadoInvitacion;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.exception.ConflictoException;
import com.presupuestos.presupuestosBackend.mapper.InvitacionMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Invitacion;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.InvitacionRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class InvitacionService {

    private final InvitacionRepository invitacionRepository;
    private final InvitacionMapper invitacionMapper;

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    public InvitacionService(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository, InvitacionRepository invitacionRepository, InvitacionMapper invitacionMapper){

        this.invitacionRepository = invitacionRepository;
        this.invitacionMapper = invitacionMapper;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional 
    public InvitacionResponseDTO crearInvitacion(InvitacionRequestDTO dto, Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNoEncontradoException("No se ha podido encontrar al usuario."));

        if(usuario.getRol() == TipoRol.USUARIO){
            throw new AccesoDenegadoException("El usuario con el rol USUARIO no puede hacer invitaciones");
        }

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId()).orElseThrow(() -> new RecursoNoEncontradoException("No se ha podido encontrar la empresa"));

        if(usuario.getRol() != TipoRol.SUPERADMIN){
            if(!usuario.getEmpresa().getId().equals(empresa.getId())){
                throw new AccesoDenegadoException("El usuario no pertenece a esta empresa");
            }
        }

        if(dto.getRol() == TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("No se pueden crear invitaciones con rol SUPERADMIN");
        }

        if(usuario.getRol() == TipoRol.ADMIN && dto.getRol() != TipoRol.USUARIO){
            throw new AccesoDenegadoException("Un ADMIN solo puede invitar usuarios con rol USUARIO");
        }

        if(usuario.getRol() == TipoRol.PROPIETARIO && dto.getRol() == TipoRol.PROPIETARIO){
            throw new AccesoDenegadoException("Un PROPIETARIO no puede invitar otro PROPIETARIO");
        }

        if(usuarioRepository.existsByEmail(dto.getEmail())){
            throw new ConflictoException("Existe un usuario con ese email");
        }

        Optional<Invitacion> invitacionPendiente = invitacionRepository.findByEmailAndEmpresaAndEstadoInvitacion(dto.getEmail(), empresa, EstadoInvitacion.PENDIENTE);

        LocalDateTime ahora = LocalDateTime.now();

        if(invitacionPendiente.isPresent()){

            Invitacion invitacionExistente = invitacionPendiente.get();

            if(invitacionExistente.getFechaExpiracion().isBefore(ahora) || invitacionExistente.getFechaExpiracion().isEqual(ahora)){
                
                invitacionExistente.setEstadoInvitacion(EstadoInvitacion.CADUCADA);
        
                invitacionRepository.save(invitacionExistente);
            }
            else{
                throw new ConflictoException("Ya existe una invitación pendiente para este usuario");
            }
        }

        Invitacion nuevaInvitacion = crearNuevaInvitacion(dto.getEmail(), empresa, dto.getRol());

        Invitacion invitacionGuardar = invitacionRepository.save(nuevaInvitacion);

        return invitacionMapper.toResponseDTO(invitacionGuardar);
    }

    private Invitacion crearNuevaInvitacion(String email, Empresa empresa, TipoRol rol){

        LocalDateTime ahora = LocalDateTime.now();

        Invitacion invitacion = new Invitacion();

        invitacion.setEmail(email);
        invitacion.setEmpresa(empresa);
        invitacion.setRol(rol);
        invitacion.setToken(UUID.randomUUID().toString());
        invitacion.setFechaCreacion(ahora);
        invitacion.setFechaUltimoEnvio(ahora);
        invitacion.setFechaExpiracion(ahora.plusHours(3));
        invitacion.setEstadoInvitacion(EstadoInvitacion.PENDIENTE);

        return invitacion;
    }

    @Transactional 
    public InvitacionResponseDTO reenviarInvitacion(Long id, Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNoEncontradoException("No se ha enconntrado al usuario"));

        Invitacion invitacion = invitacionRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException("No se ha encontrado la invitación"));

        if(usuario.getRol() == TipoRol.USUARIO) {
            throw new AccesoDenegadoException("El usuario no tiene los permisos suficientes");
        }

        if(usuario.getRol() != TipoRol.SUPERADMIN && !usuario.getEmpresa().getId().equals(invitacion.getEmpresa().getId())){
            throw new AccesoDenegadoException("El usuario no pertenece a la misma empresa");
        }

        if(invitacion.getRol() == TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("No se pueden gestionar invitaciones con rol SUPERADMIN");
        }

        if(usuario.getRol() == TipoRol.ADMIN && invitacion.getRol() != TipoRol.USUARIO){
            throw new AccesoDenegadoException("Un ADMIN solo puede gestionar invitaciones con rol USUARIO");
        }

        if(usuario.getRol() == TipoRol.PROPIETARIO && invitacion.getRol() == TipoRol.PROPIETARIO){
            throw new AccesoDenegadoException("Un PROPIETARIO no puede gestionar invitaciones con rol PROPIETARIO");
        }

        if(invitacion.getEstadoInvitacion() != EstadoInvitacion.PENDIENTE){
            throw new ConflictoException("La invitación no está pendiente y no se puede reenviar");
        }

        LocalDateTime ahora = LocalDateTime.now();

        if(invitacion.getFechaExpiracion().isBefore(ahora) || invitacion.getFechaExpiracion().isEqual(ahora)){

            invitacion.setEstadoInvitacion(EstadoInvitacion.CADUCADA);

            invitacionRepository.save(invitacion);
        }
        else{

            if(invitacion.getFechaUltimoEnvio().plusMinutes(3).isAfter(ahora)){

                throw new ConflictoException("No se puede enviar otra invitación antes de los 3 minutos");
            }

            invitacion.setFechaUltimoEnvio(ahora);

            Invitacion invitacionActualizada = invitacionRepository.save(invitacion);

            return invitacionMapper.toResponseDTO(invitacionActualizada);
        }

        Invitacion nuevaInvitacion = crearNuevaInvitacion(invitacion.getEmail(), invitacion.getEmpresa(), invitacion.getRol());

        Invitacion invitacionGuardar = invitacionRepository.save(nuevaInvitacion);

        return invitacionMapper.toResponseDTO(invitacionGuardar);
    }
}
