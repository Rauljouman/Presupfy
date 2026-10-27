package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;
import com.presupuestos.presupuestosBackend.dto.UsuarioResponseDTO;
import com.presupuestos.presupuestosBackend.exception.ConflictoException;
import com.presupuestos.presupuestosBackend.mapper.UsuarioMapper;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.dto.UsuarioRequestDTO;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public List<Usuario> listarUsuario(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioId(Long id){ 
        return usuarioRepository.findById(id);
    }

    public Usuario actualizarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void elimianUsuario(Long id){
        usuarioRepository.deleteById(id);
    }

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto){

        if(usuarioRepository.existsByEmail(dto.getEmail())){
            throw new ConflictoException("Ya existe un usuario con este correo");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setPrimerApellido(dto.getPrimerApellido());
        usuario.setSegundoApellido(dto.getSegundoApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }
}