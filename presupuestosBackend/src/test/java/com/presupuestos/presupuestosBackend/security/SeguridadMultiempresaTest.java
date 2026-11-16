package com.presupuestos.presupuestosBackend.security;

import com.presupuestos.presupuestosBackend.dto.ClienteRequestDTO;
import com.presupuestos.presupuestosBackend.dto.PresupuestoRequestDTO;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.mapper.ClienteMapper;
import com.presupuestos.presupuestosBackend.mapper.PresupuestoMapper;
import com.presupuestos.presupuestosBackend.model.Cliente;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Presupuesto;
import com.presupuestos.presupuestosBackend.repository.ClienteRepository;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.PresupuestoRepository;
import com.presupuestos.presupuestosBackend.repository.ProductoServicioRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;
import com.presupuestos.presupuestosBackend.service.ClienteService;
import com.presupuestos.presupuestosBackend.service.PresupuestoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeguridadMultiempresaTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private EmpresaRepository empresaRepository;
    @Mock private PresupuestoRepository presupuestoRepository;
    @Mock private ProductoServicioRepository productoServicioRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ClienteMapper clienteMapper;
    @Mock private PresupuestoMapper presupuestoMapper;

    private ClienteService clienteService;
    private PresupuestoService presupuestoService;

    @BeforeEach
    void configurar() {
        clienteService = new ClienteService(clienteRepository, empresaRepository, clienteMapper);
        presupuestoService = new PresupuestoService(productoServicioRepository, presupuestoMapper,
                usuarioRepository, presupuestoRepository, clienteRepository, empresaRepository);
    }

    private Empresa empresa(Long id) {
        Empresa empresa = new Empresa();
        empresa.setId(id);
        return empresa;
    }

    private Cliente cliente(Long id, Empresa empresa) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setEmpresa(empresa);
        return cliente;
    }

    @Test
    void buscarClienteDeOtraEmpresaDebeDenegarAcceso() {
        Empresa empresa1 = empresa(1L);
        Cliente clienteEmpresa2 = cliente(20L, empresa(2L));
        when(clienteRepository.findById(20L)).thenReturn(Optional.of(clienteEmpresa2));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa1));

        assertThrows(AccesoDenegadoException.class, () -> clienteService.buscarCliente(20L, 1L));
        verifyNoInteractions(clienteMapper);
    }

    @Test
    void eliminarClienteDeOtraEmpresaDebeDenegarAcceso() {
        Empresa empresa1 = empresa(1L);
        Cliente clienteEmpresa2 = cliente(20L, empresa(2L));
        when(clienteRepository.findById(20L)).thenReturn(Optional.of(clienteEmpresa2));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa1));

        assertThrows(AccesoDenegadoException.class, () -> clienteService.eliminarCliente(20L, 1L));
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }

    @Test
    void actualizarClienteDeOtraEmpresaDebeDenegarAcceso() {
        Empresa empresa1 = empresa(1L);
        Cliente clienteEmpresa2 = cliente(20L, empresa(2L));
        when(clienteRepository.findById(20L)).thenReturn(Optional.of(clienteEmpresa2));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa1));

        assertThrows(AccesoDenegadoException.class,
                () -> clienteService.actualizarCliente(20L, new ClienteRequestDTO(), 1L));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void buscarPresupuestoDeOtraEmpresaDebeDenegarAcceso() {
        Empresa empresa1 = empresa(1L);
        Presupuesto presupuestoEmpresa2 = new Presupuesto();
        presupuestoEmpresa2.setEmpresa(empresa(2L));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa1));
        when(presupuestoRepository.findById(30L)).thenReturn(Optional.of(presupuestoEmpresa2));

        assertThrows(AccesoDenegadoException.class,
                () -> presupuestoService.buscarPresupuesto(30L, 1L));
        verifyNoInteractions(presupuestoMapper);
    }

    @Test
    void modificarPresupuestoDeOtraEmpresaDebeDenegarAcceso() {
        Empresa empresa1 = empresa(1L);
        Presupuesto presupuestoEmpresa2 = new Presupuesto();
        presupuestoEmpresa2.setEmpresa(empresa(2L));
        when(presupuestoRepository.findById(30L)).thenReturn(Optional.of(presupuestoEmpresa2));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa1));

        assertThrows(AccesoDenegadoException.class,
                () -> presupuestoService.modificarPresupuesto(30L, new PresupuestoRequestDTO(), 1L));
        verify(presupuestoRepository, never()).save(any(Presupuesto.class));
    }

    @Test
    void eliminarPresupuestoDeOtraEmpresaDebeDenegarAcceso() {
        Empresa empresa1 = empresa(1L);
        Presupuesto presupuestoEmpresa2 = new Presupuesto();
        presupuestoEmpresa2.setEmpresa(empresa(2L));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa1));
        when(presupuestoRepository.findById(30L)).thenReturn(Optional.of(presupuestoEmpresa2));

        assertThrows(AccesoDenegadoException.class,
                () -> presupuestoService.eliminarPresupuesto(30L, 1L));
        verify(presupuestoRepository, never()).delete(any(Presupuesto.class));
    }

    @Test
    void crearPresupuestoConClienteDeOtraEmpresaDebeDenegarAcceso() {
        Empresa empresa1 = empresa(1L);
        Cliente clienteEmpresa2 = cliente(20L, empresa(2L));
        PresupuestoRequestDTO dto = new PresupuestoRequestDTO();
        dto.setClienteId(20L);
        when(clienteRepository.findById(20L)).thenReturn(Optional.of(clienteEmpresa2));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa1));

        assertThrows(AccesoDenegadoException.class,
                () -> presupuestoService.crearPresupuesto(dto, 5L, 1L));
        verify(presupuestoRepository, never()).save(any(Presupuesto.class));
    }
}
