package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

import com.presupuestos.presupuestosBackend.model.Empresa;


import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

import com.presupuestos.presupuestosBackend.dto.UsuarioResponseDTO;
import com.presupuestos.presupuestosBackend.mapper.UsuarioMapper;
import com.presupuestos.presupuestosBackend.dto.UsuarioRequestDTO;

import com.presupuestos.presupuestosBackend.model.Usuario;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
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
            throw new RuntimeException("Ya existe un usuario con este correo");
        }

        Long empresaId = dto.getEmpresaId();

        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaId);

        if(empresaEncontrada.isEmpty()){
            throw new RuntimeException("Error, no se ha encontrado la empresa");
        }

        Empresa empresa = empresaEncontrada.get();

        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setPrimerApellido(dto.getPrimerApellido());
        usuario.setSegundoApellido(dto.getSegundoApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        usuario.setEmpresa(empresa);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }
}