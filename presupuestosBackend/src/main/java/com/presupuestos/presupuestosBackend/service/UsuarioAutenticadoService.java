
package com.presupuestos.presupuestosBackend.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;


@Service
public class UsuarioAutenticadoService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    public UsuarioAutenticadoService(UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
    }

    public Usuario obtenerUsuarioAutenticado(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)){
            throw new AccesoDenegadoException("El usuario no está autenticado");
        }

        UUID authUserId;

        try{
            authUserId = UUID.fromString(jwt.getSubject());
        }catch(IllegalArgumentException e){
            throw new AccesoDenegadoException("El identificador del usuario autenticado no es válido");
        }

        return usuarioRepository.findByAuthUserId(authUserId).orElseThrow(() -> new RecursoNoEncontradoException("El usuario autenticado no está registrado en el CRM"));
    }

    public Long obtenerEmpresaIdAutenticada(){
        
        Usuario usuario = obtenerUsuarioAutenticado();

        if(usuario.getRol() == TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("No tienes el rol adecuado");
        }

        if(usuario.getEmpresa() == null){
            throw new RecursoNoEncontradoException("No se ha encontrado la empresa del usuario");
        }
        
        return usuario.getEmpresa().getId();
    }
}
