package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;
import com.presupuestos.presupuestosBackend.model.Usuario;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuario(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioId(Long id){ 
        return usuarioRepository.findById(id);
    }

    public Usuario crearUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void elimianUsuario(Long id){
        usuarioRepository.deleteById(id);
    }
}