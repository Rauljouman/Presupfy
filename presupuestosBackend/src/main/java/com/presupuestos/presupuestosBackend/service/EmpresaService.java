package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import com.presupuestos.presupuestosBackend.dto.EmpresaRequestDTO;
import com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.mapper.EmpresaMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper  empresaMapper;
    private final UsuarioRepository usuarioRepository;

    public EmpresaService(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository, EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public List<EmpresaResponseDTO> listarEmpresa(Long usuario_id){

        Usuario usuario =  usuarioRepository.findById(usuario_id).orElseThrow(()-> new RecursoNoEncontradoException("No se ha encontrado al usuario"));

        if(usuario.getRol() != TipoRol.SUPERADMIN){
            throw new AccesoDenegadoException("Este usuario no tiene el rol de SUPERADMIN");
        }

        List<EmpresaResponseDTO> listaEmpresaDTO = new ArrayList<>();

        List<Empresa>listaEmpresa =  empresaRepository.findAll();

        for (Empresa empresa : listaEmpresa) {
                listaEmpresaDTO.add(empresaMapper.toResponseDTO(empresa)); 
        }

        return listaEmpresaDTO;
    }

    public Optional<Empresa> buscarEmpresaId(Long id){
        return this.empresaRepository.findById(id);
    }


    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO dto){

        if(empresaRepository.existsByNif(dto.getNif())){
            throw new IllegalArgumentException("El NIF ya esta registrado");
        }

        if(empresaRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("El correo ya esta registrado");
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

    public Empresa actualizarEmpresa(Empresa empresa){
        return empresaRepository.save(empresa);
    }

    public void eliminarEmpresa(Long id){
        empresaRepository.deleteById(id);
    }
}