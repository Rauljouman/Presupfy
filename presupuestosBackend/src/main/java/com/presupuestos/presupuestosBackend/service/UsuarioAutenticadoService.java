
package com.presupuestos.presupuestosBackend.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

@Service
public class UsuarioAutenticadoService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioAutenticadoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
}
