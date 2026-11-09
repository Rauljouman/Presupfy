package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.model.UsuarioEmpresa;
import com.presupuestos.presupuestosBackend.repository.UsuarioEmpresaRepository;

@Service
public class UsuarioEmpresaService {

    private final UsuarioEmpresaRepository usuarioEmpresaRepository;

    public UsuarioEmpresaService(UsuarioEmpresaRepository usuarioEmpresaRepository) {
        this.usuarioEmpresaRepository = usuarioEmpresaRepository;
    }

    public void comprobarAcceso(Usuario usuario, Empresa empresa){

        if(usuario.isSuperAdmin()){
            return;
        }

        usuarioEmpresaRepository.findByUsuarioAndEmpresa(usuario, empresa).orElseThrow(() -> new AccesoDenegadoException("No perteneces a esta empresa"));
    }

    public void comprobarPropietario(Usuario usuario, Empresa empresa){

        if(usuario.isSuperAdmin()){
            return;
        }

        UsuarioEmpresa relacion = usuarioEmpresaRepository.findByUsuarioAndEmpresa(usuario, empresa).orElseThrow(() -> new AccesoDenegadoException("No perteneces a esta empresa"));

        if(relacion.getRol() != TipoRol.PROPIETARIO){
            throw new AccesoDenegadoException("No tienes permisos de propietario");
        }
    }

    public void comprobarSuperAdmin(Usuario usuario){

        if(!usuario.isSuperAdmin()){
            throw new AccesoDenegadoException("Solo un SUPERADMIN puede realizar esta operación");
        }
    }
}