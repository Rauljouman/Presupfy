
package com.presupuestos.presupuestosBackend.security;

import com.presupuestos.presupuestosBackend.dto.UsuarioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.UsuarioResponseDTO;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.mapper.UsuarioMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;
import com.presupuestos.presupuestosBackend.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void usuarioPuedeModificarSuPropioPerfil() {

        Usuario usuario = mock(Usuario.class);
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        UsuarioResponseDTO respuesta = mock(UsuarioResponseDTO.class);

        dto.setNombre("Raul");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuario.getId()).thenReturn(1L);

        when(usuarioMapper.toResponseDTO(usuario))
                .thenReturn(respuesta);

        UsuarioResponseDTO resultado =
                usuarioService.actualizarUsuario(1L, dto, 1L);

        assertSame(respuesta, resultado);
        verify(usuario).setNombre("Raul");
    }

    @Test
    void usuarioNoPuedeModificarOtroPerfil() {

        Usuario usuario = mock(Usuario.class);
        Usuario otroUsuario = mock(Usuario.class);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(otroUsuario));

        when(usuario.getId()).thenReturn(1L);
        when(otroUsuario.getId()).thenReturn(2L);
        when(usuario.getRol()).thenReturn(TipoRol.USUARIO);

        assertThrows(
                AccesoDenegadoException.class,
                () -> usuarioService.actualizarUsuario(
                        2L,
                        new UsuarioRequestDTO(),
                        1L
                )
        );

        verify(otroUsuario, never()).setNombre(any());
    }

    @Test
    void superadminPuedeModificarOtroPerfil() {

        Usuario superadmin = mock(Usuario.class);
        Usuario otroUsuario = mock(Usuario.class);

        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Carlos");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(superadmin));

        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(otroUsuario));

        when(superadmin.getId()).thenReturn(1L);
        when(otroUsuario.getId()).thenReturn(2L);
        when(superadmin.getRol()).thenReturn(TipoRol.SUPERADMIN);

        usuarioService.actualizarUsuario(2L, dto, 1L);

        verify(otroUsuario).setNombre("Carlos");
    }

    @Test
    void propietarioPuedeCambiarUsuarioAAdminEnSuEmpresa() {

        Usuario propietario = mock(Usuario.class);
        Usuario empleado = mock(Usuario.class);
        Empresa empresa = mock(Empresa.class);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(propietario));

        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(empleado));

        when(propietario.getRol()).thenReturn(TipoRol.PROPIETARIO);
        when(propietario.getId()).thenReturn(1L);
        when(empleado.getId()).thenReturn(2L);
        when(empleado.getRol()).thenReturn(TipoRol.USUARIO);

        when(propietario.getEmpresa()).thenReturn(empresa);
        when(empleado.getEmpresa()).thenReturn(empresa);
        when(empresa.getId()).thenReturn(1L);

        usuarioService.cambiarRolUsuario(2L, TipoRol.ADMIN, 1L);

        verify(empleado).setRol(TipoRol.ADMIN);
    }

    @Test
    void propietarioNoPuedeCambiarRolDeOtraEmpresa() {

        Usuario propietario = mock(Usuario.class);
        Usuario empleado = mock(Usuario.class);

        Empresa empresa1 = mock(Empresa.class);
        Empresa empresa2 = mock(Empresa.class);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(propietario));

        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(empleado));

        when(propietario.getRol()).thenReturn(TipoRol.PROPIETARIO);

        // Los usuarios deben tener identificadores diferentes.
        when(propietario.getId()).thenReturn(1L);
        when(empleado.getId()).thenReturn(2L);

        // Cada usuario pertenece a una empresa distinta.
        when(propietario.getEmpresa()).thenReturn(empresa1);
        when(empleado.getEmpresa()).thenReturn(empresa2);

        when(empresa1.getId()).thenReturn(1L);
        when(empresa2.getId()).thenReturn(2L);

        assertThrows(
                AccesoDenegadoException.class,
                () -> usuarioService.cambiarRolUsuario(
                        2L,
                        TipoRol.ADMIN,
                        1L
                )
        );

        verify(empleado, never()).setRol(any(TipoRol.class));
    }

    @Test
    void superadminPuedeAsignarPropietario() {

        Usuario superadmin = mock(Usuario.class);
        Usuario empleado = mock(Usuario.class);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(superadmin));

        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(empleado));

        when(superadmin.getRol()).thenReturn(TipoRol.SUPERADMIN);
        when(superadmin.getId()).thenReturn(1L);
        when(empleado.getId()).thenReturn(2L);
        when(empleado.getRol()).thenReturn(TipoRol.ADMIN);

        usuarioService.cambiarRolUsuario(
                2L,
                TipoRol.PROPIETARIO,
                1L
        );

        verify(empleado).setRol(TipoRol.PROPIETARIO);
    }
}
