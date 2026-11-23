
package com.presupuestos.presupuestosBackend.security;

import com.presupuestos.presupuestosBackend.dto.ProductoServicioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.ProductoServicioResponseDTO;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.mapper.ProductoServicioMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.ProductoServicio;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.ProductoServicioRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;
import com.presupuestos.presupuestosBackend.service.ProductoServicioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServicioServiceTest {

    @Mock
    private ProductoServicioRepository productoServicioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoServicioMapper productoServicioMapper;

    @InjectMocks
    private ProductoServicioService productoServicioService;

    @Test
    void propietarioPuedeCrearProducto() {

        Empresa empresa = mock(Empresa.class);
        Usuario usuario = mock(Usuario.class);

        ProductoServicioRequestDTO dto = new ProductoServicioRequestDTO();
        ProductoServicioResponseDTO respuesta = mock(ProductoServicioResponseDTO.class);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuario.getRol()).thenReturn(TipoRol.PROPIETARIO);
        when(usuario.getEmpresa()).thenReturn(empresa);
        when(empresa.getId()).thenReturn(1L);
        when(empresa.isActivo()).thenReturn(true);

        when(productoServicioRepository.save(any(ProductoServicio.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        when(productoServicioMapper.toResponseDTO(any(ProductoServicio.class)))
                .thenReturn(respuesta);

        ProductoServicioResponseDTO resultado =
                productoServicioService.crearProductoServicio(dto, 1L);

        assertSame(respuesta, resultado);

        verify(productoServicioRepository).save(any(ProductoServicio.class));
    }

    @Test
    void adminPuedeModificarProductoDeSuEmpresa() {

        Empresa empresa = mock(Empresa.class);
        Usuario usuario = mock(Usuario.class);
        ProductoServicio producto = mock(ProductoServicio.class);

        ProductoServicioRequestDTO dto = new ProductoServicioRequestDTO();
        ProductoServicioResponseDTO respuesta = mock(ProductoServicioResponseDTO.class);

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));
        when(usuario.getRol()).thenReturn(TipoRol.ADMIN);
        when(usuario.getEmpresa()).thenReturn(empresa);

        when(productoServicioRepository.findById(10L))
                .thenReturn(Optional.of(producto));

        when(producto.getEmpresa()).thenReturn(empresa);
        when(empresa.getId()).thenReturn(1L);
        when(empresa.isActivo()).thenReturn(true);
        when(producto.isActivo()).thenReturn(true);

        when(productoServicioMapper.toResponseDTO(producto))
                .thenReturn(respuesta);

        ProductoServicioResponseDTO resultado =
                productoServicioService.actualizarProductoServicio(10L, dto, 2L);

        assertSame(respuesta, resultado);

        verify(producto).setNombre(dto.getNombre());
    }

    @Test
    void usuarioNoPuedeModificarProducto() {

        Empresa empresa = mock(Empresa.class);
        Usuario usuario = mock(Usuario.class);
        ProductoServicio producto = mock(ProductoServicio.class);

        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));
        when(usuario.getRol()).thenReturn(TipoRol.USUARIO);

        when(productoServicioRepository.findById(10L))
                .thenReturn(Optional.of(producto));

        when(producto.getEmpresa()).thenReturn(empresa);

        assertThrows(
                AccesoDenegadoException.class,
                () -> productoServicioService.actualizarProductoServicio(
                        10L,
                        new ProductoServicioRequestDTO(),
                        3L
                )
        );

        verify(producto, never()).setNombre(any());
    }

    @Test
    void superadminNoPuedeCrearProducto() {

        Usuario usuario = mock(Usuario.class);

        when(usuarioRepository.findById(4L)).thenReturn(Optional.of(usuario));
        when(usuario.getRol()).thenReturn(TipoRol.SUPERADMIN);

        assertThrows(
                AccesoDenegadoException.class,
                () -> productoServicioService.crearProductoServicio(
                        new ProductoServicioRequestDTO(),
                        4L
                )
        );

        verify(productoServicioRepository, never())
                .save(any(ProductoServicio.class));
    }

    @Test
    void usuarioNoPuedeConsultarProductoDeOtraEmpresa() {

        Empresa empresaUsuario = mock(Empresa.class);
        Empresa empresaProducto = mock(Empresa.class);

        Usuario usuario = mock(Usuario.class);
        ProductoServicio producto = mock(ProductoServicio.class);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuario.getEmpresa()).thenReturn(empresaUsuario);

        when(empresaUsuario.getId()).thenReturn(1L);
        when(empresaProducto.getId()).thenReturn(2L);

        when(productoServicioRepository.findById(20L))
                .thenReturn(Optional.of(producto));

        when(producto.getEmpresa()).thenReturn(empresaProducto);

        assertThrows(
                AccesoDenegadoException.class,
                () -> productoServicioService.buscarProductoServicioId(20L, 1L)
        );

        verifyNoInteractions(productoServicioMapper);
    }

    @Test
    void usuarioNoPuedeDesactivarProductoDeOtraEmpresa() {

        Empresa empresaUsuario = mock(Empresa.class);
        Empresa empresaProducto = mock(Empresa.class);

        Usuario usuario = mock(Usuario.class);
        ProductoServicio producto = mock(ProductoServicio.class);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuario.getRol()).thenReturn(TipoRol.ADMIN);
        when(usuario.getEmpresa()).thenReturn(empresaUsuario);

        when(empresaUsuario.getId()).thenReturn(1L);
        when(empresaProducto.getId()).thenReturn(2L);

        when(productoServicioRepository.findById(20L))
                .thenReturn(Optional.of(producto));

        when(producto.getEmpresa()).thenReturn(empresaProducto);

        assertThrows(
                AccesoDenegadoException.class,
                () -> productoServicioService.desactivarProductoServicio(20L, 1L)
        );

        verify(producto, never()).setActivo(false);
    }
}
