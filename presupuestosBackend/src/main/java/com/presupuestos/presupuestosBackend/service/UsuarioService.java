
package com.presupuestos.presupuestosBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.dto.UsuarioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.UsuarioResponseDTO;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.ConflictoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.exception.SolicitudInvalidaException;
import com.presupuestos.presupuestosBackend.mapper.UsuarioMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Invitacion;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EmpresaRepository empresaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public UsuarioResponseDTO crearUsuarioDesdeInvitacion(UsuarioRequestDTO dto, Invitacion invitacion){

        if(usuarioRepository.existsByEmail(invitacion.getEmail())){
            throw new ConflictoException("Ya existe un usuario con este email");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setPrimerApellido(dto.getPrimerApellido());
        usuario.setSegundoApellido(dto.getSegundoApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEmail(invitacion.getEmail());
        usuario.setRol(invitacion.getRol());
        usuario.setEmpresa(invitacion.getEmpresa());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    public List<UsuarioResponseDTO> listarUsuario(Long empresaId, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        Empresa empresa = empresaRepository.findById(empresaId).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la empresa"));

        comprobarAccesoEmpresa(usuario, empresa);

        List<UsuarioResponseDTO> usuariosDTO = new ArrayList<>();

        for(Usuario usuarioEmpresa : usuarioRepository.findByEmpresa(empresa)){
            usuariosDTO.add(usuarioMapper.toResponseDTO(usuarioEmpresa));
        }

        return usuariosDTO;
    }

    public UsuarioResponseDTO buscarUsuarioId(Long id, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        Usuario usuarioEncontrado = buscarUsuario(id);

        comprobarAccesoUsuario(usuario, usuarioEncontrado);

        return usuarioMapper.toResponseDTO(usuarioEncontrado);
    }

    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        Usuario usuarioActualizar = buscarUsuario(id);

        if(!usuario.getId().equals(usuarioActualizar.getId()) && usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("No tienes permisos para actualizar este usuario");
        }

        if(dto.getNombre() == null || dto.getNombre().isBlank()){
            throw new SolicitudInvalidaException("El nombre es obligatorio");
        }

        usuarioActualizar.setNombre(dto.getNombre());
        usuarioActualizar.setPrimerApellido(dto.getPrimerApellido());
        usuarioActualizar.setSegundoApellido(dto.getSegundoApellido());
        usuarioActualizar.setTelefono(dto.getTelefono());

        return usuarioMapper.toResponseDTO(usuarioActualizar);
    }

    @Transactional
    public UsuarioResponseDTO cambiarRolUsuario(Long id, TipoRol nuevoRol, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        Usuario usuarioModificar = buscarUsuario(id);

        if(usuario.getRol() != TipoRol.SUPERADMIN){

            if(usuario.getRol() != TipoRol.PROPIETARIO){
                throw new AccesoDenegadoException("No tienes permisos para modificar roles");
            }

            comprobarAccesoUsuario(usuario, usuarioModificar);
        }

        if(usuario.getId().equals(usuarioModificar.getId())){
            throw new AccesoDenegadoException("No puedes modificar tu propio rol");
        }

        if(nuevoRol == null || nuevoRol == TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("No puedes asignar este rol");
        }

        if(usuario.getRol() != TipoRol.SUPERADMIN && nuevoRol == TipoRol.PROPIETARIO){
            throw new AccesoDenegadoException("Solo un SUPERADMIN puede asignar el rol PROPIETARIO");
        }

        if(usuarioModificar.getRol() == TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("No se puede modificar el rol de un SUPERADMIN");
        }

        if(usuarioModificar.getRol() == nuevoRol){
            throw new ConflictoException("El usuario ya tiene este rol");
        }

        if(usuarioModificar.getRol() == TipoRol.PROPIETARIO && nuevoRol != TipoRol.PROPIETARIO){
            throw new ConflictoException("No se puede quitar directamente el rol PROPIETARIO sin comprobar antes la titularidad de la empresa");
        }

        usuarioModificar.setRol(nuevoRol);

        return usuarioMapper.toResponseDTO(usuarioModificar);
    }

    private Usuario buscarUsuario(Long id){
        return usuarioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el usuario"));
    }

    private void comprobarAccesoEmpresa(Usuario usuario, Empresa empresa){

        if(usuario.getRol() == TipoRol.SUPERADMIN){
            return;
        }

        if(usuario.getEmpresa() == null || !usuario.getEmpresa().getId().equals(empresa.getId())){
            throw new AccesoDenegadoException("No tienes acceso a esta empresa");
        }
    }

    private void comprobarAccesoUsuario(Usuario usuario, Usuario usuarioEncontrado){

        if(usuario.getRol() == TipoRol.SUPERADMIN || usuario.getId().equals(usuarioEncontrado.getId())){
            return;
        }

        if(usuario.getEmpresa() == null || usuarioEncontrado.getEmpresa() == null || !usuario.getEmpresa().getId().equals(usuarioEncontrado.getEmpresa().getId())){
            throw new AccesoDenegadoException("No tienes acceso a este usuario");
        }
    }
}
