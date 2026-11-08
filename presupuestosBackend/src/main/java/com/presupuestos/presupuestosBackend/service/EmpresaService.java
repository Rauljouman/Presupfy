package com.presupuestos.presupuestosBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.dto.EmpresaRequestDTO;
import com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.ConflictoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.mapper.EmpresaMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;
    private final UsuarioRepository usuarioRepository;

    public EmpresaService(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository, EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public List<EmpresaResponseDTO> listarEmpresa(Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado al usuario"));

        if(usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("Este usuario no tiene el rol de SUPERADMIN");
        }

        List<EmpresaResponseDTO> listaEmpresaDTO = new ArrayList<>();

        List<Empresa> listaEmpresa = empresaRepository.findAll();

        for (Empresa empresa : listaEmpresa) {
            listaEmpresaDTO.add(empresaMapper.toResponseDTO(empresa));
        }

        return listaEmpresaDTO;
    }


    public EmpresaResponseDTO buscarEmpresaId(Long id, Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el usuario"));

        if(usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("Solo un SUPERADMIN puede consultar empresas");
        }

        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la empresa"));

        return empresaMapper.toResponseDTO(empresa);
    }


    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO dto, Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el usuario"));

        if(usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("El usuario no tiene permisos suficientes para crear la empresa");
        }

        if(empresaRepository.existsByNif(dto.getNif())){
            throw new ConflictoException("El NIF ya está registrado");
        }

        if(empresaRepository.existsByEmail(dto.getEmail())){
            throw new ConflictoException("El correo ya está registrado");
        }

        Empresa empresa = new Empresa();

        empresa.setRazonSocial(dto.getRazonSocial());
        empresa.setNombreComercial(dto.getNombreComercial());
        empresa.setNif(dto.getNif());
        empresa.setDireccion(dto.getDireccion());
        empresa.setCodigoPostal(dto.getCodigoPostal());
        empresa.setCiudad(dto.getCiudad());
        empresa.setProvincia(dto.getProvincia());
        empresa.setPais(dto.getPais());
        empresa.setTelefono(dto.getTelefono());
        empresa.setEmail(dto.getEmail());
        empresa.setLogoUrl(dto.getLogoUrl());
        empresa.setActivo(true);

        Empresa empresaGuardar = empresaRepository.save(empresa);

        return empresaMapper.toResponseDTO(empresaGuardar);
    }


    @Transactional
    public EmpresaResponseDTO actualizarEmpresa(Long id, EmpresaRequestDTO dto, Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado al usuario"));

        if(usuario.getRol() != TipoRol.PROPIETARIO && usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("No tienes permisos suficientes para actualizar la empresa.");
        }

        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la empresa"));

        if(usuario.getRol() == TipoRol.PROPIETARIO && !usuario.getEmpresa().getId().equals(empresa.getId())){
            throw new AccesoDenegadoException("No puedes modificar una empresa a la que no perteneces");
        }

        if(empresaRepository.existsByNifAndIdNot(dto.getNif(), id)){
            throw new ConflictoException("El NIF ya está registrado en otra empresa");
        }

        if(empresaRepository.existsByEmailAndIdNot(dto.getEmail(), id)){
            throw new ConflictoException("El correo ya está registrado en otra empresa");
        }

        empresa.setRazonSocial(dto.getRazonSocial());
        empresa.setNombreComercial(dto.getNombreComercial());
        empresa.setNif(dto.getNif());
        empresa.setDireccion(dto.getDireccion());
        empresa.setCodigoPostal(dto.getCodigoPostal());
        empresa.setCiudad(dto.getCiudad());
        empresa.setProvincia(dto.getProvincia());
        empresa.setPais(dto.getPais());
        empresa.setTelefono(dto.getTelefono());
        empresa.setEmail(dto.getEmail());
        empresa.setLogoUrl(dto.getLogoUrl());

        Empresa empresaGuardar = empresaRepository.save(empresa);

        return empresaMapper.toResponseDTO(empresaGuardar);
    }


    @Transactional
    public void eliminarEmpresa(Long id, Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado al usuario"));

        if(usuario.getRol() != TipoRol.PROPIETARIO && usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("No tienes permisos suficientes para eliminar la empresa");
        }

        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la empresa"));

        if(usuario.getRol() == TipoRol.PROPIETARIO && !usuario.getEmpresa().getId().equals(empresa.getId())){
            throw new AccesoDenegadoException("No puedes eliminar una empresa a la que no perteneces");
        }

        empresa.setActivo(false);

        empresaRepository.save(empresa);
    }

    @Transactional
    public void reactivarEmpresa(Long id, Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(()-> new RecursoNoEncontradoException("No se ha encotrado al usuario"));

        if(usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("El usuario no tiene permisos suficientes para modificar el estado de la empresa");
        }

        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la empresa"));

        if(empresa.isActivo()){
            throw new ConflictoException("La empresa esta activa");
        }

        empresa.setActivo(true);
    }

    @Transactional
    public void desactivarEmpresa(Long id, Long usuarioId){

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(()-> new RecursoNoEncontradoException("No se ha encotrado al usuario"));

        if(usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("El usuario no tiene permisos suficientes para modificar el estado de la empresa");
        }

        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la empresa"));

        if(!empresa.isActivo()){
            throw new ConflictoException("La empresa esta desactivada");
        }

        empresa.setActivo(false);
    }
}