
package com.presupuestos.presupuestosBackend.service;

import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.presupuestos.presupuestosBackend.dto.EmpresaRequestDTO;
import com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO;
import com.presupuestos.presupuestosBackend.dto.RegistroEmpresaRequestDTO;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.ConflictoException;
import com.presupuestos.presupuestosBackend.mapper.EmpresaMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

@Service
public class RegistroEmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaMapper empresaMapper;
    private final SupabaseAuthService supabaseAuthService;

    public RegistroEmpresaService(EmpresaRepository empresaRepository,
                                  UsuarioRepository usuarioRepository,
                                  EmpresaMapper empresaMapper,
                                  SupabaseAuthService supabaseAuthService) {

        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.empresaMapper = empresaMapper;
        this.supabaseAuthService = supabaseAuthService;
    }

    @Transactional
    public EmpresaResponseDTO registrarEmpresa(RegistroEmpresaRequestDTO dto, Jwt jwt){

        UUID authUserId;

        try{
            authUserId = UUID.fromString(jwt.getSubject());
        }catch(IllegalArgumentException | NullPointerException e){
            throw new AccesoDenegadoException("El identificador del usuario autenticado no es válido");
        }

        String emailAutenticado = jwt.getClaimAsString("email");

        if(emailAutenticado == null || emailAutenticado.isBlank()){
            throw new AccesoDenegadoException("No se ha encontrado el correo del usuario autenticado");
        }

        emailAutenticado = emailAutenticado.trim();

        supabaseAuthService.verificarUsuario(jwt.getTokenValue(), authUserId, emailAutenticado);

        if(usuarioRepository.existsByAuthUserId(authUserId)){
            throw new ConflictoException("Este usuario ya está registrado en el CRM");
        }

        if(usuarioRepository.existsByEmailIgnoreCase(emailAutenticado)){
            throw new ConflictoException("Este correo ya está registrado en el CRM");
        }

        EmpresaRequestDTO empresaDTO = dto.getEmpresa();

        if(empresaRepository.existsByNif(empresaDTO.getNif())){
            throw new ConflictoException("El NIF ya está registrado");
        }

        if(empresaRepository.existsByEmail(empresaDTO.getEmail())){
            throw new ConflictoException("El correo de la empresa ya está registrado");
        }

        Empresa empresa = new Empresa();

        empresa.setRazonSocial(empresaDTO.getRazonSocial());
        empresa.setNombreComercial(empresaDTO.getNombreComercial());
        empresa.setNif(empresaDTO.getNif());
        empresa.setDireccion(empresaDTO.getDireccion());
        empresa.setCodigoPostal(empresaDTO.getCodigoPostal());
        empresa.setCiudad(empresaDTO.getCiudad());
        empresa.setProvincia(empresaDTO.getProvincia());
        empresa.setPais(empresaDTO.getPais());
        empresa.setTelefono(empresaDTO.getTelefono());
        empresa.setEmail(empresaDTO.getEmail());
        empresa.setLogoUrl(empresaDTO.getLogoUrl());
        empresa.setActivo(true);

        Empresa empresaGuardada = empresaRepository.save(empresa);

        Usuario usuario = new Usuario();

        usuario.setAuthUserId(authUserId);
        usuario.setNombre(dto.getNombre());
        usuario.setPrimerApellido(dto.getPrimerApellido());
        usuario.setSegundoApellido(dto.getSegundoApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEmail(emailAutenticado);
        usuario.setRol(TipoRol.PROPIETARIO);
        usuario.setEmpresa(empresaGuardada);

        usuarioRepository.save(usuario);

        return empresaMapper.toResponseDTO(empresaGuardada);
    }
}
