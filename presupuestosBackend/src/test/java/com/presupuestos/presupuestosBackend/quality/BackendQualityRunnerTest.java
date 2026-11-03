package com.presupuestos.presupuestosBackend.quality;

import com.presupuestos.presupuestosBackend.controller.ClienteController;
import com.presupuestos.presupuestosBackend.controller.EmpresaController;
import com.presupuestos.presupuestosBackend.controller.InvitacionController;
import com.presupuestos.presupuestosBackend.controller.PresupuestoController;
import com.presupuestos.presupuestosBackend.dto.ClienteRequestDTO;
import com.presupuestos.presupuestosBackend.dto.EmpresaRequestDTO;
import com.presupuestos.presupuestosBackend.dto.InvitacionRequestDTO;
import com.presupuestos.presupuestosBackend.dto.InvitacionResponseDTO;
import com.presupuestos.presupuestosBackend.dto.LineaPresupuestoRequestDTO;
import com.presupuestos.presupuestosBackend.dto.PresupuestoRequestDTO;
import com.presupuestos.presupuestosBackend.dto.UsuarioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.UsuarioResponseDTO;
import com.presupuestos.presupuestosBackend.enums.EstadoInvitacion;
import com.presupuestos.presupuestosBackend.enums.EstadoPresupuesto;
import com.presupuestos.presupuestosBackend.enums.TipoCliente;
import com.presupuestos.presupuestosBackend.enums.TipoProductoServicio;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.ConflictoException;
import com.presupuestos.presupuestosBackend.exception.GlobalExceptionHandler;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.exception.SolicitudInvalidaException;
import com.presupuestos.presupuestosBackend.mapper.ClienteMapper;
import com.presupuestos.presupuestosBackend.mapper.EmpresaMapper;
import com.presupuestos.presupuestosBackend.mapper.InvitacionMapper;
import com.presupuestos.presupuestosBackend.mapper.LineaPresupuestoMapper;
import com.presupuestos.presupuestosBackend.mapper.PresupuestoMapper;
import com.presupuestos.presupuestosBackend.model.Cliente;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Invitacion;
import com.presupuestos.presupuestosBackend.model.Presupuesto;
import com.presupuestos.presupuestosBackend.model.ProductoServicio;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.ClienteRepository;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.InvitacionRepository;
import com.presupuestos.presupuestosBackend.repository.PresupuestoRepository;
import com.presupuestos.presupuestosBackend.repository.ProductoServicioRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;
import com.presupuestos.presupuestosBackend.service.ClienteService;
import com.presupuestos.presupuestosBackend.service.EmpresaService;
import com.presupuestos.presupuestosBackend.service.InvitacionService;
import com.presupuestos.presupuestosBackend.service.PresupuestoService;
import com.presupuestos.presupuestosBackend.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Batería ejecutable de calidad para el estado actual del CRM.
 *
 * Se limita intencionadamente a:
 * - Cliente
 * - Empresa
 * - Invitación
 * - Presupuesto
 *
 * UsuarioService y ProductoServicio solo aparecen como dependencias mockeadas
 * cuando alguno de los cuatro módulos los necesita.
 *
 * Ejecutar con:
 *   mvn -Dtest=BackendQualityRunnerTest test
 */
public class BackendQualityRunnerTest {

    private final Informe informe = new Informe();

    @Test
    void ejecutarBateriaCompleta() throws Exception {
        try {
            pruebasClienteService();
            pruebasClienteController();
            pruebasEmpresaService();
            pruebasEmpresaController();
            pruebasInvitacionService();
            pruebasInvitacionController();
            pruebasPresupuestoService();
            pruebasPresupuestoController();
        } finally {
            informe.imprimir();
        }

        if (informe.totalFallos() > 0) {
            throw new AssertionError("La batería ha detectado " + informe.totalFallos()
                    + " fallo(s). Revisa el informe anterior.");
        }
    }

    // ============================================================
    // CLIENTE - SERVICE
    // ============================================================

    private void pruebasClienteService() {
        ClienteFixture f = new ClienteFixture();

        f.reset();
        Empresa e1 = empresa(1L);
        Cliente c1 = clienteParticular(10L, e1);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.clienteRepository.findByEmpresa(e1)).thenReturn(List.of(c1));
        casoOk(Modulo.CLIENTE, Capa.SERVICE, "listar clientes de empresa existente", () -> {
            var lista = f.service.listarCliente(1L);
            check(lista.size() == 1, "Debe devolver un cliente");
            check(lista.get(0).getEmpresaId().equals(1L), "El DTO debe conservar empresaId");
        });

        f.reset();
        when(f.empresaRepository.findById(99L)).thenReturn(Optional.empty());
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "listar con empresa inexistente",
                RecursoNoEncontradoException.class, () -> f.service.listarCliente(99L));

        f.reset();
        e1 = empresa(1L);
        c1 = clienteParticular(10L, e1);
        when(f.clienteRepository.findById(10L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        casoOk(Modulo.CLIENTE, Capa.SERVICE, "buscar cliente de su empresa", () -> {
            var dto = f.service.buscarCliente(10L, 1L);
            check(dto.getId().equals(10L), "Debe devolver el cliente solicitado");
        });

        f.reset();
        when(f.clienteRepository.findById(404L)).thenReturn(Optional.empty());
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "buscar cliente inexistente",
                RecursoNoEncontradoException.class, () -> f.service.buscarCliente(404L, 1L));

        f.reset();
        e1 = empresa(1L);
        c1 = clienteParticular(10L, e1);
        when(f.clienteRepository.findById(10L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(999L)).thenReturn(Optional.empty());
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "buscar cliente con empresa inexistente",
                RecursoNoEncontradoException.class, () -> f.service.buscarCliente(10L, 999L));

        f.reset();
        Empresa e2 = empresa(2L);
        c1 = clienteParticular(10L, empresa(1L));
        when(f.clienteRepository.findById(10L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(2L)).thenReturn(Optional.of(e2));
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "bloquear lectura de cliente de otra empresa",
                AccesoDenegadoException.class, () -> f.service.buscarCliente(10L, 2L));

        f.reset();
        e1 = empresa(1L);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.clienteRepository.save(any(Cliente.class))).thenAnswer(i -> {
            Cliente c = i.getArgument(0);
            c.setId(20L);
            return c;
        });
        ClienteRequestDTO particular = dtoClienteParticular(1L);
        ClienteRequestDTO particularValido = particular;
        casoOk(Modulo.CLIENTE, Capa.SERVICE, "crear cliente particular válido", () -> {
            var dto = f.service.crearCliente(particularValido);
            check(dto.getTipoCliente() == TipoCliente.PARTICULAR, "Debe ser PARTICULAR");
            check("12345678Z".equals(dto.getDni()), "Debe conservar DNI");
        });

        f.reset();
        particular = dtoClienteParticular(1L);
        particular.setNombre(" ");
        ClienteRequestDTO particularSinNombre = particular;
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "rechazar particular sin nombre",
                SolicitudInvalidaException.class, () -> f.service.crearCliente(particularSinNombre));

        f.reset();
        particular = dtoClienteParticular(1L);
        particular.setDni(null);
        ClienteRequestDTO particularSinDni = particular;
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "rechazar particular sin DNI",
                SolicitudInvalidaException.class, () -> f.service.crearCliente(particularSinDni));

        f.reset();
        e1 = empresa(1L);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.clienteRepository.save(any(Cliente.class))).thenAnswer(i -> {
            Cliente c = i.getArgument(0);
            c.setId(21L);
            return c;
        });
        ClienteRequestDTO empresaDto = dtoClienteEmpresa(1L);
        ClienteRequestDTO empresaValida = empresaDto;
        casoOk(Modulo.CLIENTE, Capa.SERVICE, "crear cliente empresa válido", () -> {
            var dto = f.service.crearCliente(empresaValida);
            check(dto.getTipoCliente() == TipoCliente.EMPRESA, "Debe ser EMPRESA");
            check("B12345678".equals(dto.getNif()), "Debe conservar NIF");
        });

        f.reset();
        empresaDto = dtoClienteEmpresa(1L);
        empresaDto.setRazonSocial("");
        ClienteRequestDTO empresaSinRazon = empresaDto;
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "rechazar cliente empresa sin razón social",
                SolicitudInvalidaException.class, () -> f.service.crearCliente(empresaSinRazon));

        f.reset();
        empresaDto = dtoClienteEmpresa(1L);
        empresaDto.setNif(null);
        ClienteRequestDTO empresaSinNif = empresaDto;
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "rechazar cliente empresa sin NIF",
                SolicitudInvalidaException.class, () -> f.service.crearCliente(empresaSinNif));

        f.reset();
        when(f.empresaRepository.findById(77L)).thenReturn(Optional.empty());
        ClienteRequestDTO clienteEmpresaInexistente = dtoClienteParticular(77L);
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "crear cliente en empresa inexistente",
                RecursoNoEncontradoException.class, () -> f.service.crearCliente(clienteEmpresaInexistente));

        f.reset();
        e1 = empresa(1L);
        c1 = clienteEmpresa(30L, e1);
        when(f.clienteRepository.findById(30L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));
        particular = dtoClienteParticular(1L);
        ClienteRequestDTO aParticular = particular;
        casoOk(Modulo.CLIENTE, Capa.SERVICE, "actualizar EMPRESA a PARTICULAR y limpiar campos", () -> {
            var dto = f.service.actualizarCliente(30L, aParticular);
            check(dto.getTipoCliente() == TipoCliente.PARTICULAR, "Debe cambiar a PARTICULAR");
            check(dto.getRazonSocial() == null && dto.getNif() == null,
                    "Debe limpiar razón social y NIF");
        });

        f.reset();
        e1 = empresa(1L);
        c1 = clienteParticular(31L, e1);
        when(f.clienteRepository.findById(31L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));
        empresaDto = dtoClienteEmpresa(1L);
        ClienteRequestDTO aEmpresa = empresaDto;
        casoOk(Modulo.CLIENTE, Capa.SERVICE, "actualizar PARTICULAR a EMPRESA y limpiar campos", () -> {
            var dto = f.service.actualizarCliente(31L, aEmpresa);
            check(dto.getTipoCliente() == TipoCliente.EMPRESA, "Debe cambiar a EMPRESA");
            check(dto.getNombre() == null && dto.getDni() == null,
                    "Debe limpiar campos de particular");
        });

        f.reset();
        when(f.clienteRepository.findById(555L)).thenReturn(Optional.empty());
        ClienteRequestDTO dtoActualizacion = dtoClienteParticular(1L);
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "actualizar cliente inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.actualizarCliente(555L, dtoActualizacion));

        f.reset();
        e1 = empresa(1L);
        c1 = clienteParticular(40L, e1);
        when(f.clienteRepository.findById(40L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(2L)).thenReturn(Optional.of(empresa(2L)));
        ClienteRequestDTO dtoOtraEmpresa = dtoClienteParticular(2L);
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "bloquear actualización desde otra empresa",
                AccesoDenegadoException.class,
                () -> f.service.actualizarCliente(40L, dtoOtraEmpresa));

        f.reset();
        e1 = empresa(1L);
        c1 = clienteParticular(50L, e1);
        when(f.clienteRepository.findById(50L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        Cliente clienteEliminar = c1;
        casoOk(Modulo.CLIENTE, Capa.SERVICE, "eliminar cliente de su empresa", () -> {
            f.service.eliminarCliente(50L, 1L);
            verify(f.clienteRepository).delete(clienteEliminar);
        });

        f.reset();
        when(f.clienteRepository.findById(51L)).thenReturn(Optional.empty());
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "eliminar cliente inexistente",
                RecursoNoEncontradoException.class, () -> f.service.eliminarCliente(51L, 1L));

        f.reset();
        c1 = clienteParticular(52L, empresa(1L));
        when(f.clienteRepository.findById(52L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(2L)).thenReturn(Optional.of(empresa(2L)));
        casoEx(Modulo.CLIENTE, Capa.SERVICE, "bloquear eliminación de cliente de otra empresa",
                AccesoDenegadoException.class, () -> f.service.eliminarCliente(52L, 2L));
    }

    // ============================================================
    // CLIENTE - CONTROLLER
    // ============================================================

    private void pruebasClienteController() throws Exception {
        ClienteService service = mock(ClienteService.class);
        ClienteController controller = new ClienteController(service);
        MockMvc mvc = mockMvc(controller);

        reset(service);
        when(service.listarCliente(1L)).thenReturn(List.of());
        casoHttp(Modulo.CLIENTE, "GET /api/clientes", 200,
                () -> status(mvc.perform(get("/api/clientes").param("empresId", "1"))));

        reset(service);
        when(service.buscarCliente(10L, 1L)).thenReturn(new com.presupuestos.presupuestosBackend.dto.ClienteResponseDTO());
        casoHttp(Modulo.CLIENTE, "GET /api/clientes/{id}", 200,
                () -> status(mvc.perform(get("/api/clientes/10").param("empresaId", "1"))));

        reset(service);
        when(service.crearCliente(any(ClienteRequestDTO.class)))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.ClienteResponseDTO());
        casoHttp(Modulo.CLIENTE, "POST /api/clientes válido", 200,
                () -> status(mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClienteParticular()))));

        reset(service);
        casoHttp(Modulo.CLIENTE, "POST /api/clientes sin tipo", 400,
                () -> status(mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ana\",\"dni\":\"12345678Z\",\"empresaId\":1}"))));

        reset(service);
        doThrow(new AccesoDenegadoException("otra empresa"))
                .when(service).eliminarCliente(10L, 2L);
        casoHttp(Modulo.CLIENTE, "DELETE bloqueado por multiempresa", 403,
                () -> status(mvc.perform(delete("/api/clientes/10").param("empresId", "2"))));

        reset(service);
        when(service.buscarCliente(404L, 1L))
                .thenThrow(new RecursoNoEncontradoException("no existe"));
        casoHttp(Modulo.CLIENTE, "GET cliente inexistente -> 404", 404,
                () -> status(mvc.perform(get("/api/clientes/404").param("empresaId", "1"))));
    }

    // ============================================================
    // EMPRESA - SERVICE
    // ============================================================

    private void pruebasEmpresaService() {
        EmpresaFixture f = new EmpresaFixture();

        f.reset();
        Usuario superadmin = usuario(1L, TipoRol.SUPERADMIN, null);
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findAll()).thenReturn(List.of(empresa(1L), empresa(2L)));
        casoOk(Modulo.EMPRESA, Capa.SERVICE, "SUPERADMIN lista todas las empresas", () -> {
            check(f.service.listarEmpresa(1L).size() == 2, "Debe listar dos empresas");
        });

        f.reset();
        when(f.usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "listar con usuario inexistente",
                RecursoNoEncontradoException.class, () -> f.service.listarEmpresa(99L));

        f.reset();
        when(f.usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuario(2L, TipoRol.ADMIN, empresa(1L))));
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "ADMIN no puede listar empresas",
                AccesoDenegadoException.class, () -> f.service.listarEmpresa(2L));

        f.reset();
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(5L)).thenReturn(Optional.of(empresa(5L)));
        casoOk(Modulo.EMPRESA, Capa.SERVICE, "SUPERADMIN busca empresa por id", () -> {
            check(f.service.buscarEmpresaId(5L, 1L).getId().equals(5L), "Debe devolver id 5");
        });

        f.reset();
        when(f.usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuario(2L, TipoRol.PROPIETARIO, empresa(5L))));
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "PROPIETARIO no puede usar buscarEmpresaId actual",
                AccesoDenegadoException.class, () -> f.service.buscarEmpresaId(5L, 2L));

        f.reset();
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(404L)).thenReturn(Optional.empty());
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "buscar empresa inexistente",
                RecursoNoEncontradoException.class, () -> f.service.buscarEmpresaId(404L, 1L));

        f.reset();
        when(f.empresaRepository.existsByNif("B12345678")).thenReturn(false);
        when(f.empresaRepository.existsByEmail("empresa@test.com")).thenReturn(false);
        when(f.empresaRepository.save(any(Empresa.class))).thenAnswer(i -> {
            Empresa e = i.getArgument(0);
            e.setId(10L);
            return e;
        });
        EmpresaRequestDTO empresaDto = dtoEmpresa();
        EmpresaRequestDTO empresaCrearValida = empresaDto;
        casoOk(Modulo.EMPRESA, Capa.SERVICE, "crear empresa válida", () -> {
            var dto = f.service.crearEmpresa(empresaCrearValida);
            check(dto.getId().equals(10L), "Debe devolver empresa guardada");
            check(dto.isActivo(), "Debe crearse activa");
        });

        f.reset();
        when(f.empresaRepository.existsByNif("B12345678")).thenReturn(true);
        empresaDto = dtoEmpresa();
        EmpresaRequestDTO dtoNifDuplicado = empresaDto;
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "rechazar NIF duplicado",
                ConflictoException.class, () -> f.service.crearEmpresa(dtoNifDuplicado));

        f.reset();
        when(f.empresaRepository.existsByNif("B12345678")).thenReturn(false);
        when(f.empresaRepository.existsByEmail("empresa@test.com")).thenReturn(true);
        empresaDto = dtoEmpresa();
        EmpresaRequestDTO dtoEmailDuplicado = empresaDto;
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "rechazar email duplicado",
                ConflictoException.class, () -> f.service.crearEmpresa(dtoEmailDuplicado));

        f.reset();
        Empresa e1 = empresa(1L);
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.empresaRepository.existsByNifAndIdNot(any(), eq(1L))).thenReturn(false);
        when(f.empresaRepository.existsByEmailAndIdNot(any(), eq(1L))).thenReturn(false);
        when(f.empresaRepository.save(any(Empresa.class))).thenAnswer(i -> i.getArgument(0));
        empresaDto = dtoEmpresa();
        EmpresaRequestDTO dtoActualizarSuper = empresaDto;
        Empresa empresaActualizarSuper = e1;
        casoOk(Modulo.EMPRESA, Capa.SERVICE, "SUPERADMIN actualiza empresa", () -> {
            f.service.actualizarEmpresa(1L, dtoActualizarSuper, 1L);
            verify(f.empresaRepository).save(empresaActualizarSuper);
        });

        f.reset();
        e1 = empresa(1L);
        Usuario propietario = usuario(2L, TipoRol.PROPIETARIO, e1);
        when(f.usuarioRepository.findById(2L)).thenReturn(Optional.of(propietario));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.empresaRepository.existsByNifAndIdNot(any(), eq(1L))).thenReturn(false);
        when(f.empresaRepository.existsByEmailAndIdNot(any(), eq(1L))).thenReturn(false);
        when(f.empresaRepository.save(any(Empresa.class))).thenAnswer(i -> i.getArgument(0));
        empresaDto = dtoEmpresa();
        EmpresaRequestDTO dtoActualizarProp = empresaDto;
        casoOk(Modulo.EMPRESA, Capa.SERVICE, "PROPIETARIO actualiza su empresa", () ->
                f.service.actualizarEmpresa(1L, dtoActualizarProp, 2L));

        f.reset();
        when(f.usuarioRepository.findById(3L))
                .thenReturn(Optional.of(usuario(3L, TipoRol.ADMIN, empresa(1L))));
        empresaDto = dtoEmpresa();
        EmpresaRequestDTO dtoSinPermisos = empresaDto;
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "ADMIN no puede actualizar empresa",
                AccesoDenegadoException.class,
                () -> f.service.actualizarEmpresa(1L, dtoSinPermisos, 3L));

        f.reset();
        Empresa propia = empresa(1L);
        when(f.usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuario(2L, TipoRol.PROPIETARIO, propia)));
        when(f.empresaRepository.findById(2L)).thenReturn(Optional.of(empresa(2L)));
        empresaDto = dtoEmpresa();
        EmpresaRequestDTO dtoOtra = empresaDto;
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "PROPIETARIO no actualiza otra empresa",
                AccesoDenegadoException.class,
                () -> f.service.actualizarEmpresa(2L, dtoOtra, 2L));

        f.reset();
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(empresa(1L)));
        when(f.empresaRepository.existsByNifAndIdNot("B12345678", 1L)).thenReturn(true);
        empresaDto = dtoEmpresa();
        EmpresaRequestDTO dtoNifOtra = empresaDto;
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "actualizar con NIF de otra empresa",
                ConflictoException.class,
                () -> f.service.actualizarEmpresa(1L, dtoNifOtra, 1L));

        f.reset();
        e1 = empresa(1L);
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.empresaRepository.save(any(Empresa.class))).thenAnswer(i -> i.getArgument(0));
        Empresa empresaDesactivar = e1;
        casoOk(Modulo.EMPRESA, Capa.SERVICE, "eliminar es baja lógica", () -> {
            f.service.eliminarEmpresa(1L, 1L);
            check(!empresaDesactivar.isActivo(), "Debe quedar activo=false");
            verify(f.empresaRepository).save(empresaDesactivar);
        });

        f.reset();
        when(f.usuarioRepository.findById(3L))
                .thenReturn(Optional.of(usuario(3L, TipoRol.USUARIO, empresa(1L))));
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "USUARIO no puede eliminar empresa",
                AccesoDenegadoException.class, () -> f.service.eliminarEmpresa(1L, 3L));

        f.reset();
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(404L)).thenReturn(Optional.empty());
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "eliminar empresa inexistente",
                RecursoNoEncontradoException.class, () -> f.service.eliminarEmpresa(404L, 1L));

        f.reset();
        propia = empresa(1L);
        when(f.usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuario(2L, TipoRol.PROPIETARIO, propia)));
        when(f.empresaRepository.findById(2L)).thenReturn(Optional.of(empresa(2L)));
        casoEx(Modulo.EMPRESA, Capa.SERVICE, "PROPIETARIO no elimina otra empresa",
                AccesoDenegadoException.class, () -> f.service.eliminarEmpresa(2L, 2L));
    }

    // ============================================================
    // EMPRESA - CONTROLLER
    // ============================================================

    private void pruebasEmpresaController() throws Exception {
        EmpresaService service = mock(EmpresaService.class);
        EmpresaController controller = new EmpresaController(service);
        MockMvc mvc = mockMvc(controller);

        reset(service);
        when(service.listarEmpresa(1L)).thenReturn(List.of());
        casoHttp(Modulo.EMPRESA, "GET /api/empresas", 200,
                () -> status(mvc.perform(get("/api/empresas").param("usuarioId", "1"))));

        reset(service);
        when(service.buscarEmpresaId(5L, 1L))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO());
        casoHttp(Modulo.EMPRESA, "GET /api/empresas/{id}", 200,
                () -> status(mvc.perform(get("/api/empresas/5").param("usuarioId", "1"))));

        reset(service);
        when(service.crearEmpresa(any(EmpresaRequestDTO.class)))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO());
        casoHttp(Modulo.EMPRESA, "POST /api/empresas válido", 200,
                () -> status(mvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpresa()))));

        reset(service);
        when(service.actualizarEmpresa(eq(5L), any(EmpresaRequestDTO.class), eq(1L)))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.EmpresaResponseDTO());
        casoHttp(Modulo.EMPRESA, "PUT /api/empresas/{id}", 200,
                () -> status(mvc.perform(put("/api/empresas/5")
                        .param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpresa()))));

        reset(service);
        doThrow(new AccesoDenegadoException("sin permisos"))
                .when(service).eliminarEmpresa(5L, 3L);
        casoHttp(Modulo.EMPRESA, "DELETE sin permisos -> 403", 403,
                () -> status(mvc.perform(delete("/api/empresas/5").param("usuarioId", "3"))));

        reset(service);
        when(service.crearEmpresa(any(EmpresaRequestDTO.class)))
                .thenThrow(new ConflictoException("duplicada"));
        casoHttp(Modulo.EMPRESA, "POST empresa duplicada -> 409", 409,
                () -> status(mvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpresa()))));
    }

    // ============================================================
    // INVITACION - SERVICE
    // ============================================================

    private void pruebasInvitacionService() {
        InvitacionFixture f = new InvitacionFixture();
        Empresa e1 = empresa(1L);

        f.reset();
        Usuario superadmin = usuario(1L, TipoRol.SUPERADMIN, null);
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.usuarioRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        when(f.invitacionRepository.findByEmailAndEmpresaAndEstadoInvitacion(
                "nuevo@test.com", e1, EstadoInvitacion.PENDIENTE)).thenReturn(Optional.empty());
        when(f.invitacionRepository.save(any(Invitacion.class))).thenAnswer(i -> {
            Invitacion inv = i.getArgument(0);
            if (inv.getId() == null) inv.setId(100L);
            return inv;
        });
        InvitacionRequestDTO invDto = dtoInvitacion("nuevo@test.com", TipoRol.ADMIN, 1L);
        InvitacionRequestDTO invCrearValida = invDto;
        casoOk(Modulo.INVITACION, Capa.SERVICE, "SUPERADMIN crea invitación", () -> {
            InvitacionResponseDTO dto = f.service.crearInvitacion(invCrearValida, 1L);
            check(dto.getId().equals(100L), "Debe devolver invitación guardada");
            check(dto.getEstadoInvitacion() == EstadoInvitacion.PENDIENTE,
                    "El response debe incluir estado PENDIENTE");
        });

        f.reset();
        when(f.usuarioRepository.findById(404L)).thenReturn(Optional.empty());
        invDto = dtoInvitacion("nuevo@test.com", TipoRol.USUARIO, 1L);
        InvitacionRequestDTO invUsuarioNoExiste = invDto;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "crear invitación con usuario inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.crearInvitacion(invUsuarioNoExiste, 404L));

        f.reset();
        when(f.usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuario(2L, TipoRol.USUARIO, e1)));
        invDto = dtoInvitacion("nuevo@test.com", TipoRol.USUARIO, 1L);
        InvitacionRequestDTO invPorUsuario = invDto;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "rol USUARIO no crea invitaciones",
                AccesoDenegadoException.class,
                () -> f.service.crearInvitacion(invPorUsuario, 2L));

        f.reset();
        when(f.usuarioRepository.findById(3L))
                .thenReturn(Optional.of(usuario(3L, TipoRol.ADMIN, e1)));
        when(f.empresaRepository.findById(2L)).thenReturn(Optional.of(empresa(2L)));
        invDto = dtoInvitacion("nuevo@test.com", TipoRol.USUARIO, 2L);
        InvitacionRequestDTO invOtraEmpresa = invDto;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "no invitar en otra empresa",
                AccesoDenegadoException.class,
                () -> f.service.crearInvitacion(invOtraEmpresa, 3L));

        f.reset();
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        invDto = dtoInvitacion("root@test.com", TipoRol.SUPERADMIN, 1L);
        InvitacionRequestDTO invSuperadmin = invDto;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "prohibir invitación SUPERADMIN",
                AccesoDenegadoException.class,
                () -> f.service.crearInvitacion(invSuperadmin, 1L));

        f.reset();
        when(f.usuarioRepository.findById(3L))
                .thenReturn(Optional.of(usuario(3L, TipoRol.ADMIN, e1)));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        invDto = dtoInvitacion("admin2@test.com", TipoRol.ADMIN, 1L);
        InvitacionRequestDTO adminInvitaAdmin = invDto;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "ADMIN solo invita USUARIO",
                AccesoDenegadoException.class,
                () -> f.service.crearInvitacion(adminInvitaAdmin, 3L));

        f.reset();
        when(f.usuarioRepository.findById(4L))
                .thenReturn(Optional.of(usuario(4L, TipoRol.PROPIETARIO, e1)));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        invDto = dtoInvitacion("prop2@test.com", TipoRol.PROPIETARIO, 1L);
        InvitacionRequestDTO propInvitaProp = invDto;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "PROPIETARIO no invita PROPIETARIO",
                AccesoDenegadoException.class,
                () -> f.service.crearInvitacion(propInvitaProp, 4L));

        f.reset();
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.usuarioRepository.existsByEmail("existe@test.com")).thenReturn(true);
        invDto = dtoInvitacion("existe@test.com", TipoRol.USUARIO, 1L);
        InvitacionRequestDTO invEmailExistente = invDto;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "no invitar email ya registrado",
                ConflictoException.class,
                () -> f.service.crearInvitacion(invEmailExistente, 1L));

        f.reset();
        Invitacion pendiente = invitacion(20L, "nuevo@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().plusHours(1));
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.usuarioRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        when(f.invitacionRepository.findByEmailAndEmpresaAndEstadoInvitacion(
                "nuevo@test.com", e1, EstadoInvitacion.PENDIENTE)).thenReturn(Optional.of(pendiente));
        invDto = dtoInvitacion("nuevo@test.com", TipoRol.USUARIO, 1L);
        InvitacionRequestDTO invPendienteActiva = invDto;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "bloquear invitación pendiente activa duplicada",
                ConflictoException.class,
                () -> f.service.crearInvitacion(invPendienteActiva, 1L));

        f.reset();
        Invitacion caducadaPrev = invitacion(21L, "nuevo@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusHours(4),
                LocalDateTime.now().minusMinutes(1));
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.usuarioRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        when(f.invitacionRepository.findByEmailAndEmpresaAndEstadoInvitacion(
                "nuevo@test.com", e1, EstadoInvitacion.PENDIENTE)).thenReturn(Optional.of(caducadaPrev));
        when(f.invitacionRepository.save(any(Invitacion.class))).thenAnswer(i -> i.getArgument(0));
        invDto = dtoInvitacion("nuevo@test.com", TipoRol.USUARIO, 1L);
        InvitacionRequestDTO invTrasCaducada = invDto;
        casoOk(Modulo.INVITACION, Capa.SERVICE, "caducar invitación antigua y crear nueva", () -> {
            f.service.crearInvitacion(invTrasCaducada, 1L);
            check(caducadaPrev.getEstadoInvitacion() == EstadoInvitacion.CADUCADA,
                    "La anterior debe quedar CADUCADA");
        });

        f.reset();
        Invitacion reenviable = invitacion(30L, "r@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(2));
        reenviable.setFechaUltimoEnvio(LocalDateTime.now().minusMinutes(4));
        when(f.usuarioRepository.findById(4L))
                .thenReturn(Optional.of(usuario(4L, TipoRol.PROPIETARIO, e1)));
        when(f.invitacionRepository.findById(30L)).thenReturn(Optional.of(reenviable));
        when(f.invitacionRepository.save(reenviable)).thenReturn(reenviable);
        casoOk(Modulo.INVITACION, Capa.SERVICE, "reenviar tras cooldown conserva invitación", () -> {
            LocalDateTime antes = reenviable.getFechaUltimoEnvio();
            f.service.reenviarInvitacion(30L, 4L);
            check(reenviable.getFechaUltimoEnvio().isAfter(antes), "Debe actualizar fechaUltimoEnvio");
        });

        f.reset();
        Invitacion cooldown = invitacion(31L, "r@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().plusHours(2));
        cooldown.setFechaUltimoEnvio(LocalDateTime.now().minusMinutes(1));
        when(f.usuarioRepository.findById(4L))
                .thenReturn(Optional.of(usuario(4L, TipoRol.PROPIETARIO, e1)));
        when(f.invitacionRepository.findById(31L)).thenReturn(Optional.of(cooldown));
        casoEx(Modulo.INVITACION, Capa.SERVICE, "bloquear reenvío antes de 3 minutos",
                ConflictoException.class, () -> f.service.reenviarInvitacion(31L, 4L));

        f.reset();
        Invitacion aceptada = invitacion(32L, "r@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.ACEPTADA, LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1));
        when(f.usuarioRepository.findById(4L))
                .thenReturn(Optional.of(usuario(4L, TipoRol.PROPIETARIO, e1)));
        when(f.invitacionRepository.findById(32L)).thenReturn(Optional.of(aceptada));
        casoEx(Modulo.INVITACION, Capa.SERVICE, "no reenviar invitación aceptada",
                ConflictoException.class, () -> f.service.reenviarInvitacion(32L, 4L));

        f.reset();
        Invitacion expirada = invitacion(33L, "r@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusHours(5),
                LocalDateTime.now().minusMinutes(1));
        when(f.usuarioRepository.findById(4L))
                .thenReturn(Optional.of(usuario(4L, TipoRol.PROPIETARIO, e1)));
        when(f.invitacionRepository.findById(33L)).thenReturn(Optional.of(expirada));
        when(f.invitacionRepository.findByEmailAndEmpresaAndEstadoInvitacion(
                "r@test.com", e1, EstadoInvitacion.PENDIENTE)).thenReturn(Optional.empty());
        when(f.invitacionRepository.save(any(Invitacion.class))).thenAnswer(i -> i.getArgument(0));
        casoOk(Modulo.INVITACION, Capa.SERVICE, "reenviar expirada crea invitación nueva", () -> {
            f.service.reenviarInvitacion(33L, 4L);
            check(expirada.getEstadoInvitacion() == EstadoInvitacion.CADUCADA,
                    "La original debe quedar CADUCADA");
        });

        f.reset();
        UsuarioRequestDTO alta = dtoUsuarioInvitacion("token-ok");
        Invitacion invAceptar = invitacion(40L, "alta@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().plusHours(1));
        invAceptar.setToken("token-ok");
        when(f.invitacionRepository.findByToken("token-ok")).thenReturn(Optional.of(invAceptar));
        when(f.usuarioRepository.existsByEmail("alta@test.com")).thenReturn(false);
        when(f.usuarioService.crearUsuarioDesdeInvitacion(alta, invAceptar))
                .thenReturn(new UsuarioResponseDTO());
        when(f.invitacionRepository.save(invAceptar)).thenReturn(invAceptar);
        UsuarioRequestDTO altaValida = alta;
        casoOk(Modulo.INVITACION, Capa.SERVICE, "aceptar invitación pendiente válida", () -> {
            f.service.aceptarInvitacion(altaValida);
            check(invAceptar.getEstadoInvitacion() == EstadoInvitacion.ACEPTADA,
                    "Debe marcar ACEPTADA");
        });

        f.reset();
        alta = dtoUsuarioInvitacion("token-no");
        when(f.invitacionRepository.findByToken("token-no")).thenReturn(Optional.empty());
        UsuarioRequestDTO tokenNoExiste = alta;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "aceptar token inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.aceptarInvitacion(tokenNoExiste));

        f.reset();
        alta = dtoUsuarioInvitacion("token-used");
        Invitacion usada = invitacion(41L, "alta@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.ACEPTADA, LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1));
        usada.setToken("token-used");
        when(f.invitacionRepository.findByToken("token-used")).thenReturn(Optional.of(usada));
        UsuarioRequestDTO tokenUsado = alta;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "no aceptar invitación ya aceptada",
                ConflictoException.class, () -> f.service.aceptarInvitacion(tokenUsado));

        f.reset();
        alta = dtoUsuarioInvitacion("token-exp");
        Invitacion invExp = invitacion(42L, "alta@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusHours(5),
                LocalDateTime.now().minusMinutes(1));
        invExp.setToken("token-exp");
        when(f.invitacionRepository.findByToken("token-exp")).thenReturn(Optional.of(invExp));
        UsuarioRequestDTO tokenExp = alta;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "no aceptar invitación caducada por fecha",
                ConflictoException.class, () -> f.service.aceptarInvitacion(tokenExp));

        f.reset();
        alta = dtoUsuarioInvitacion("token-email");
        Invitacion invEmail = invitacion(43L, "alta@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusMinutes(5),
                LocalDateTime.now().plusHours(1));
        invEmail.setToken("token-email");
        when(f.invitacionRepository.findByToken("token-email")).thenReturn(Optional.of(invEmail));
        when(f.usuarioRepository.existsByEmail("alta@test.com")).thenReturn(true);
        UsuarioRequestDTO emailYaRegistrado = alta;
        casoEx(Modulo.INVITACION, Capa.SERVICE, "no aceptar si email ya existe",
                ConflictoException.class, () -> f.service.aceptarInvitacion(emailYaRegistrado));

        f.reset();
        Invitacion vencida1 = invitacion(50L, "a@test.com", TipoRol.USUARIO, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusHours(5),
                LocalDateTime.now().minusMinutes(3));
        Invitacion vencida2 = invitacion(51L, "b@test.com", TipoRol.ADMIN, e1,
                EstadoInvitacion.PENDIENTE, LocalDateTime.now().minusHours(6),
                LocalDateTime.now().minusMinutes(1));
        when(f.invitacionRepository.findByEstadoInvitacionAndFechaExpiracionLessThanEqual(
                eq(EstadoInvitacion.PENDIENTE), any(LocalDateTime.class)))
                .thenReturn(List.of(vencida1, vencida2));
        casoOk(Modulo.INVITACION, Capa.SERVICE, "scheduler caduca todas las pendientes vencidas", () -> {
            f.service.caducarInvitacionesExpiradas();
            check(vencida1.getEstadoInvitacion() == EstadoInvitacion.CADUCADA, "La primera debe caducar");
            check(vencida2.getEstadoInvitacion() == EstadoInvitacion.CADUCADA, "La segunda debe caducar");
            verify(f.invitacionRepository).saveAll(any());
        });

        f.reset();
        when(f.usuarioRepository.findById(1L)).thenReturn(Optional.of(superadmin));
        when(f.invitacionRepository.findAll()).thenReturn(List.of(invAceptar));
        casoOk(Modulo.INVITACION, Capa.SERVICE, "SUPERADMIN lista todas las invitaciones", () -> {
            check(f.service.listarInvitaciones(1L).size() == 1, "Debe listar una invitación");
        });

        f.reset();
        Usuario propietario = usuario(4L, TipoRol.PROPIETARIO, e1);
        when(f.usuarioRepository.findById(4L)).thenReturn(Optional.of(propietario));
        when(f.invitacionRepository.findByEmpresa(e1)).thenReturn(List.of(invAceptar));
        casoOk(Modulo.INVITACION, Capa.SERVICE, "PROPIETARIO lista solo su empresa", () -> {
            f.service.listarInvitaciones(4L);
            verify(f.invitacionRepository).findByEmpresa(e1);
            verify(f.invitacionRepository, never()).findAll();
        });

        f.reset();
        when(f.usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuario(2L, TipoRol.USUARIO, e1)));
        casoEx(Modulo.INVITACION, Capa.SERVICE, "USUARIO no lista invitaciones",
                AccesoDenegadoException.class, () -> f.service.listarInvitaciones(2L));
    }

    // ============================================================
    // INVITACION - CONTROLLER
    // ============================================================

    private void pruebasInvitacionController() throws Exception {
        InvitacionService service = mock(InvitacionService.class);
        InvitacionController controller = new InvitacionController(service);
        MockMvc mvc = mockMvc(controller);

        reset(service);
        InvitacionResponseDTO response = new InvitacionResponseDTO();
        response.setId(1L);
        response.setEmail("nuevo@test.com");
        response.setRol(TipoRol.USUARIO);
        response.setEmpresaId(1L);
        response.setEstadoInvitacion(EstadoInvitacion.PENDIENTE);
        when(service.crearInvitacion(any(InvitacionRequestDTO.class), eq(1L))).thenReturn(response);
        casoHttp(Modulo.INVITACION, "POST /api/invitaciones", 200,
                () -> status(mvc.perform(post("/api/invitaciones")
                        .param("usuarioId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvitacion()))));

        reset(service);
        when(service.listarInvitaciones(1L)).thenReturn(List.of());
        casoHttp(Modulo.INVITACION, "GET /api/invitaciones", 200,
                () -> status(mvc.perform(get("/api/invitaciones").param("usuarioId", "1"))));

        reset(service);
        when(service.reenviarInvitacion(5L, 1L)).thenReturn(response);
        casoHttp(Modulo.INVITACION, "POST /api/invitaciones/{id}/reenviar", 200,
                () -> status(mvc.perform(post("/api/invitaciones/5/reenviar").param("usuarioId", "1"))));

        reset(service);
        when(service.aceptarInvitacion(any(UsuarioRequestDTO.class))).thenReturn(new UsuarioResponseDTO());
        casoHttp(Modulo.INVITACION, "POST /api/invitaciones/aceptar válido", 200,
                () -> status(mvc.perform(post("/api/invitaciones/aceptar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUsuarioAceptacion(true)))));

        reset(service);
        when(service.crearInvitacion(any(InvitacionRequestDTO.class), eq(2L)))
                .thenThrow(new AccesoDenegadoException("sin permisos"));
        casoHttp(Modulo.INVITACION, "crear invitación sin permisos -> 403", 403,
                () -> status(mvc.perform(post("/api/invitaciones")
                        .param("usuarioId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvitacion()))));

        reset(service);
        when(service.reenviarInvitacion(5L, 1L))
                .thenThrow(new ConflictoException("cooldown"));
        casoHttp(Modulo.INVITACION, "reenvío en cooldown -> 409", 409,
                () -> status(mvc.perform(post("/api/invitaciones/5/reenviar").param("usuarioId", "1"))));

        // Contrato funcional acordado: el email debe venir de la invitación, no del frontend.
        // El alta por invitación no reenvía el email: el backend lo obtiene de la invitación.
        reset(service);
        when(service.aceptarInvitacion(any(UsuarioRequestDTO.class))).thenReturn(new UsuarioResponseDTO());
        casoHttp(Modulo.INVITACION, "aceptar invitación sin reenviar email desde frontend", 200,
                () -> status(mvc.perform(post("/api/invitaciones/aceptar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUsuarioAceptacion(false)))));

        // Verifica además que el estado pueda serializarse en el JSON de respuesta.
        reset(service);
        when(service.crearInvitacion(any(InvitacionRequestDTO.class), eq(1L))).thenReturn(response);
        casoOk(Modulo.INVITACION, Capa.CONTROLLER, "response de invitación expone estadoInvitacion", () -> {
            var result = mvc.perform(post("/api/invitaciones")
                            .param("usuarioId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonInvitacion()))
                    .andReturn();
            String body = result.getResponse().getContentAsString();
            check(body.contains("\"estadoInvitacion\""),
                    "El JSON debe contener estadoInvitacion");
        });
    }

    // ============================================================
    // PRESUPUESTO - SERVICE
    // ============================================================

    private void pruebasPresupuestoService() {
        PresupuestoFixture f = new PresupuestoFixture();
        Empresa e1 = empresa(1L);
        Cliente c1 = clienteParticular(10L, e1);
        Usuario u1 = usuario(20L, TipoRol.USUARIO, e1);

        f.reset();
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findByEmpresa(e1)).thenReturn(List.of(presupuesto(1L, e1, c1, u1, EstadoPresupuesto.BORRADOR)));
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "listar presupuestos de empresa", () -> {
            check(f.service.listarPresupuesto(1L).size() == 1, "Debe listar uno");
        });

        f.reset();
        when(f.empresaRepository.findById(404L)).thenReturn(Optional.empty());
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "listar con empresa inexistente",
                RecursoNoEncontradoException.class, () -> f.service.listarPresupuesto(404L));

        f.reset();
        Presupuesto p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.BORRADOR);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "buscar presupuesto de su empresa", () -> {
            check(f.service.buscarPresupuesto(1L, 1L).getId().equals(1L), "Debe devolver id 1");
        });

        f.reset();
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(404L)).thenReturn(Optional.empty());
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "buscar presupuesto inexistente",
                RecursoNoEncontradoException.class, () -> f.service.buscarPresupuesto(404L, 1L));

        f.reset();
        p1 = presupuesto(1L, empresa(2L), clienteParticular(10L, empresa(2L)),
                usuario(20L, TipoRol.USUARIO, empresa(2L)), EstadoPresupuesto.BORRADOR);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "bloquear consulta de presupuesto de otra empresa",
                AccesoDenegadoException.class, () -> f.service.buscarPresupuesto(1L, 1L));

        f.reset();
        prepararCreacionBase(f, e1, c1, u1);
        when(f.presupuestoRepository.findFirstByEmpresaAndFechaCreacionBetweenOrderByNumeroPresupuestoDesc(
                eq(e1), any(LocalDate.class), any(LocalDate.class))).thenReturn(Optional.empty());
        when(f.presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(i -> {
            Presupuesto p = i.getArgument(0);
            p.setId(100L);
            return p;
        });
        PresupuestoRequestDTO presupuestoManual = dtoPresupuestoManual(1L, 10L, 20L);
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "crear primer presupuesto manual", () -> {
            var dto = f.service.crearPresupuesto(presupuestoManual);
            check(dto.getNumeroPresupuesto().endsWith("-0001"), "Debe empezar por 0001");
            check(dto.getEstado() == EstadoPresupuesto.BORRADOR, "Debe nacer BORRADOR");
            check(dto.getTotal().compareTo(new BigDecimal("121.00")) == 0,
                    "100 + 21% IVA debe totalizar 121");
        });

        f.reset();
        prepararCreacionBase(f, e1, c1, u1);
        Presupuesto anterior = presupuesto(90L, e1, c1, u1, EstadoPresupuesto.BORRADOR);
        anterior.setNumeroPresupuesto("PRE-" + LocalDate.now().getYear() + "-0009");
        anterior.setFechaCreacion(LocalDate.now());
        when(f.presupuestoRepository.findFirstByEmpresaAndFechaCreacionBetweenOrderByNumeroPresupuestoDesc(
                eq(e1), any(LocalDate.class), any(LocalDate.class))).thenReturn(Optional.of(anterior));
        when(f.presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(i -> i.getArgument(0));
        PresupuestoRequestDTO presupuestoSecuencia = dtoPresupuestoManual(1L, 10L, 20L);
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "numeración anual incrementa 0009 -> 0010", () -> {
            var dto = f.service.crearPresupuesto(presupuestoSecuencia);
            check(dto.getNumeroPresupuesto().endsWith("-0010"), "Debe generar 0010");
        });

        f.reset();
        when(f.clienteRepository.findById(999L)).thenReturn(Optional.empty());
        PresupuestoRequestDTO clienteInexistente = dtoPresupuestoManual(1L, 999L, 20L);
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "crear con cliente inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.crearPresupuesto(clienteInexistente));

        f.reset();
        when(f.clienteRepository.findById(10L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(999L)).thenReturn(Optional.empty());
        PresupuestoRequestDTO empresaInexistente = dtoPresupuestoManual(999L, 10L, 20L);
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "crear con empresa inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.crearPresupuesto(empresaInexistente));

        f.reset();
        Cliente clienteOtra = clienteParticular(10L, empresa(2L));
        when(f.clienteRepository.findById(10L)).thenReturn(Optional.of(clienteOtra));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        PresupuestoRequestDTO clienteOtraEmpresa = dtoPresupuestoManual(1L, 10L, 20L);
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "cliente debe pertenecer a empresa",
                AccesoDenegadoException.class,
                () -> f.service.crearPresupuesto(clienteOtraEmpresa));

        f.reset();
        when(f.clienteRepository.findById(10L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        PresupuestoRequestDTO usuarioInexistente = dtoPresupuestoManual(1L, 10L, 999L);
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "crear con usuario inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.crearPresupuesto(usuarioInexistente));

        f.reset();
        Usuario usuarioOtra = usuario(20L, TipoRol.USUARIO, empresa(2L));
        when(f.clienteRepository.findById(10L)).thenReturn(Optional.of(c1));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.usuarioRepository.findById(20L)).thenReturn(Optional.of(usuarioOtra));
        PresupuestoRequestDTO usuarioOtraEmpresa = dtoPresupuestoManual(1L, 10L, 20L);
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "usuario debe pertenecer a empresa",
                AccesoDenegadoException.class,
                () -> f.service.crearPresupuesto(usuarioOtraEmpresa));

        f.reset();
        prepararCreacionBase(f, e1, c1, u1);
        PresupuestoRequestDTO sinLineas = dtoPresupuestoManual(1L, 10L, 20L);
        sinLineas.setLineas(List.of());
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "presupuesto necesita al menos una línea",
                SolicitudInvalidaException.class,
                () -> f.service.crearPresupuesto(sinLineas));

        f.reset();
        prepararCreacionBase(f, e1, c1, u1);
        LineaPresupuestoRequestDTO lineaCatalogo = new LineaPresupuestoRequestDTO();
        lineaCatalogo.setProductoServicioId(77L);
        lineaCatalogo.setCantidad(BigDecimal.ONE);
        lineaCatalogo.setPorcentajeDescuento(BigDecimal.ZERO);
        PresupuestoRequestDTO productoNoExiste = dtoPresupuestoBase(1L, 10L, 20L, List.of(lineaCatalogo));
        when(f.productoServicioRepository.findById(77L)).thenReturn(Optional.empty());
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "línea catálogo con producto inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.crearPresupuesto(productoNoExiste));

        f.reset();
        prepararCreacionBase(f, e1, c1, u1);
        ProductoServicio productoOtra = producto(77L, empresa(2L));
        when(f.productoServicioRepository.findById(77L)).thenReturn(Optional.of(productoOtra));
        lineaCatalogo = new LineaPresupuestoRequestDTO();
        lineaCatalogo.setProductoServicioId(77L);
        lineaCatalogo.setCantidad(BigDecimal.ONE);
        lineaCatalogo.setPorcentajeDescuento(BigDecimal.ZERO);
        PresupuestoRequestDTO productoOtraEmpresa = dtoPresupuestoBase(1L, 10L, 20L, List.of(lineaCatalogo));
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "producto catálogo debe pertenecer a empresa",
                AccesoDenegadoException.class,
                () -> f.service.crearPresupuesto(productoOtraEmpresa));

        f.reset();
        prepararCreacionBase(f, e1, c1, u1);
        LineaPresupuestoRequestDTO lineaManualInvalida = new LineaPresupuestoRequestDTO();
        lineaManualInvalida.setNombre(" ");
        lineaManualInvalida.setPrecioUnitario(new BigDecimal("100"));
        lineaManualInvalida.setTipoIva(new BigDecimal("21"));
        lineaManualInvalida.setCantidad(BigDecimal.ONE);
        lineaManualInvalida.setPorcentajeDescuento(BigDecimal.ZERO);
        PresupuestoRequestDTO manualInvalida = dtoPresupuestoBase(1L, 10L, 20L, List.of(lineaManualInvalida));
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "línea manual necesita nombre",
                SolicitudInvalidaException.class,
                () -> f.service.crearPresupuesto(manualInvalida));

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.BORRADOR);
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.clienteRepository.findById(10L)).thenReturn(Optional.of(c1));
        when(f.presupuestoRepository.save(any(Presupuesto.class))).thenAnswer(i -> i.getArgument(0));
        PresupuestoRequestDTO modificar = dtoPresupuestoManual(1L, 10L, 20L);
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "modificar presupuesto BORRADOR", () -> {
            var dto = f.service.modificarPresupuesto(1L, modificar);
            check(dto.getLineas().size() == 1, "Debe reemplazar líneas");
        });

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.ENVIADO);
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        PresupuestoRequestDTO modificarEnviado = dtoPresupuestoManual(1L, 10L, 20L);
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "no modificar presupuesto ENVIADO",
                ConflictoException.class,
                () -> f.service.modificarPresupuesto(1L, modificarEnviado));

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.BORRADOR);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(f.presupuestoRepository.save(p1)).thenReturn(p1);
        Presupuesto enviarP = p1;
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "enviar BORRADOR -> ENVIADO", () -> {
            f.service.enviarPresupuesto(1L, 1L);
            check(enviarP.getEstado() == EstadoPresupuesto.ENVIADO, "Debe quedar ENVIADO");
            check(enviarP.getFechaEnvio() != null, "Debe establecer fechaEnvio");
        });

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.APROBADO);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "solo BORRADOR puede enviarse",
                ConflictoException.class, () -> f.service.enviarPresupuesto(1L, 1L));

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.ENVIADO);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(f.presupuestoRepository.save(p1)).thenReturn(p1);
        Presupuesto aprobarP = p1;
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "aprobar ENVIADO -> APROBADO", () -> {
            f.service.aprobarPresupuesto(1L, 1L);
            check(aprobarP.getEstado() == EstadoPresupuesto.APROBADO, "Debe quedar APROBADO");
            check(aprobarP.getFechaRespuesta() != null, "Debe establecer fechaRespuesta");
        });

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.BORRADOR);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "solo ENVIADO puede aprobarse",
                ConflictoException.class, () -> f.service.aprobarPresupuesto(1L, 1L));

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.ENVIADO);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(f.presupuestoRepository.save(p1)).thenReturn(p1);
        Presupuesto rechazarP = p1;
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "rechazar ENVIADO -> RECHAZADO", () -> {
            f.service.rechazarPresupuesto(1L, 1L);
            check(rechazarP.getEstado() == EstadoPresupuesto.RECHAZADO, "Debe quedar RECHAZADO");
        });

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.ENVIADO);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(f.presupuestoRepository.save(p1)).thenReturn(p1);
        Presupuesto caducarP = p1;
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "caducar ENVIADO -> CADUCADO", () -> {
            f.service.caducarPresupuesto(1L, 1L);
            check(caducarP.getEstado() == EstadoPresupuesto.CADUCADO, "Debe quedar CADUCADO");
        });

        f.reset();
        Presupuesto aviso = presupuesto(2L, e1, c1, u1, EstadoPresupuesto.ENVIADO);
        aviso.setFechaEnvio(LocalDate.now().minusDays(15));
        Presupuesto avisoFinal = aviso;
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "aviso de respuesta a los 15 días", () ->
                check(f.service.necesitaAvisoRespuesta(avisoFinal), "A los 15 días debe avisar"));

        f.reset();
        aviso = presupuesto(2L, e1, c1, u1, EstadoPresupuesto.ENVIADO);
        aviso.setFechaEnvio(LocalDate.now().minusDays(14));
        Presupuesto sinAviso = aviso;
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "sin aviso antes de 15 días", () ->
                check(!f.service.necesitaAvisoRespuesta(sinAviso), "A los 14 días no debe avisar"));

        f.reset();
        aviso = presupuesto(2L, e1, c1, u1, EstadoPresupuesto.BORRADOR);
        Presupuesto borradorSinAviso = aviso;
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "BORRADOR nunca necesita aviso", () ->
                check(!f.service.necesitaAvisoRespuesta(borradorSinAviso), "BORRADOR debe devolver false"));

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.BORRADOR);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        Presupuesto borrarP = p1;
        casoOk(Modulo.PRESUPUESTO, Capa.SERVICE, "eliminar presupuesto BORRADOR", () -> {
            f.service.eliminarPresupuesto(1L, 1L);
            verify(f.presupuestoRepository).delete(borrarP);
        });

        f.reset();
        p1 = presupuesto(1L, e1, c1, u1, EstadoPresupuesto.ENVIADO);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(1L)).thenReturn(Optional.of(p1));
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "no eliminar presupuesto no BORRADOR",
                ConflictoException.class, () -> f.service.eliminarPresupuesto(1L, 1L));

        f.reset();
        when(f.empresaRepository.findById(404L)).thenReturn(Optional.empty());
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "acción con empresa inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.enviarPresupuesto(1L, 404L));

        f.reset();
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(404L)).thenReturn(Optional.empty());
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "acción con presupuesto inexistente",
                RecursoNoEncontradoException.class,
                () -> f.service.enviarPresupuesto(404L, 1L));

        f.reset();
        Presupuesto deOtra = presupuesto(3L, empresa(2L), clienteParticular(30L, empresa(2L)),
                usuario(30L, TipoRol.USUARIO, empresa(2L)), EstadoPresupuesto.BORRADOR);
        when(f.empresaRepository.findById(1L)).thenReturn(Optional.of(e1));
        when(f.presupuestoRepository.findById(3L)).thenReturn(Optional.of(deOtra));
        casoEx(Modulo.PRESUPUESTO, Capa.SERVICE, "acción bloqueada sobre presupuesto de otra empresa",
                AccesoDenegadoException.class,
                () -> f.service.enviarPresupuesto(3L, 1L));
    }

    // ============================================================
    // PRESUPUESTO - CONTROLLER
    // ============================================================

    private void pruebasPresupuestoController() throws Exception {
        PresupuestoService service = mock(PresupuestoService.class);
        PresupuestoController controller = new PresupuestoController(service);
        MockMvc mvc = mockMvc(controller);

        reset(service);
        when(service.listarPresupuesto(1L)).thenReturn(List.of());
        casoHttp(Modulo.PRESUPUESTO, "GET /api/presupuestos", 200,
                () -> status(mvc.perform(get("/api/presupuestos").param("empresaId", "1"))));

        reset(service);
        when(service.crearPresupuesto(any(PresupuestoRequestDTO.class)))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO());
        casoHttp(Modulo.PRESUPUESTO, "POST /api/presupuestos", 200,
                () -> status(mvc.perform(post("/api/presupuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPresupuesto()))));

        reset(service);
        when(service.buscarPresupuesto(5L, 1L))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO());
        casoHttp(Modulo.PRESUPUESTO, "GET /api/presupuestos/{id}", 200,
                () -> status(mvc.perform(get("/api/presupuestos/5").param("empresaId", "1"))));

        reset(service);
        when(service.modificarPresupuesto(eq(5L), any(PresupuestoRequestDTO.class)))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO());
        casoHttp(Modulo.PRESUPUESTO, "PUT /api/presupuestos/{id}", 200,
                () -> status(mvc.perform(put("/api/presupuestos/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPresupuesto()))));

        reset(service);
        when(service.enviarPresupuesto(5L, 1L))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO());
        casoHttp(Modulo.PRESUPUESTO, "PATCH /enviar", 200,
                () -> status(mvc.perform(patch("/api/presupuestos/5/enviar").param("empresaId", "1"))));

        reset(service);
        when(service.aprobarPresupuesto(5L, 1L))
                .thenThrow(new ConflictoException("estado inválido"));
        casoHttp(Modulo.PRESUPUESTO, "PATCH /aprobar conflicto -> 409", 409,
                () -> status(mvc.perform(patch("/api/presupuestos/5/aprobar").param("empresaId", "1"))));

        reset(service);
        when(service.rechazarPresupuesto(5L, 1L))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO());
        casoHttp(Modulo.PRESUPUESTO, "PATCH /rechazar", 200,
                () -> status(mvc.perform(patch("/api/presupuestos/5/rechazar").param("empresaId", "1"))));

        reset(service);
        when(service.caducarPresupuesto(5L, 1L))
                .thenReturn(new com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO());
        casoHttp(Modulo.PRESUPUESTO, "PATCH /caducar", 200,
                () -> status(mvc.perform(patch("/api/presupuestos/5/caducar").param("empresaId", "1"))));

        reset(service);
        doThrow(new AccesoDenegadoException("otra empresa"))
                .when(service).eliminarPresupuesto(5L, 2L);
        casoHttp(Modulo.PRESUPUESTO, "DELETE de otra empresa -> 403", 403,
                () -> status(mvc.perform(delete("/api/presupuestos/5").param("empresaId", "2"))));
    }

    // ============================================================
    // FIXTURES
    // ============================================================

    private static class ClienteFixture {
        final ClienteRepository clienteRepository = mock(ClienteRepository.class);
        final EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
        final ClienteService service = new ClienteService(clienteRepository, empresaRepository, new ClienteMapper());

        void reset() {
            org.mockito.Mockito.reset(clienteRepository, empresaRepository);
        }
    }

    private static class EmpresaFixture {
        final EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
        final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        final EmpresaService service = new EmpresaService(empresaRepository, usuarioRepository, new EmpresaMapper());

        void reset() {
            org.mockito.Mockito.reset(empresaRepository, usuarioRepository);
        }
    }

    private static class InvitacionFixture {
        final EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
        final UsuarioService usuarioService = mock(UsuarioService.class);
        final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        final InvitacionRepository invitacionRepository = mock(InvitacionRepository.class);
        final InvitacionService service = new InvitacionService(
                empresaRepository, usuarioService, usuarioRepository, invitacionRepository, new InvitacionMapper());

        void reset() {
            org.mockito.Mockito.reset(empresaRepository, usuarioService, usuarioRepository, invitacionRepository);
        }
    }

    private static class PresupuestoFixture {
        final ProductoServicioRepository productoServicioRepository = mock(ProductoServicioRepository.class);
        final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        final PresupuestoRepository presupuestoRepository = mock(PresupuestoRepository.class);
        final ClienteRepository clienteRepository = mock(ClienteRepository.class);
        final EmpresaRepository empresaRepository = mock(EmpresaRepository.class);
        final PresupuestoMapper mapper = new PresupuestoMapper(new LineaPresupuestoMapper());
        final PresupuestoService service = new PresupuestoService(
                productoServicioRepository,
                mapper,
                usuarioRepository,
                presupuestoRepository,
                clienteRepository,
                empresaRepository);

        void reset() {
            org.mockito.Mockito.reset(productoServicioRepository, usuarioRepository, presupuestoRepository,
                    clienteRepository, empresaRepository);
        }
    }

    // ============================================================
    // FACTORÍAS DE DATOS
    // ============================================================

    private static Empresa empresa(Long id) {
        Empresa e = new Empresa();
        e.setId(id);
        e.setRazonSocial("Empresa " + id);
        e.setNombreComercial("Comercial " + id);
        e.setNif("B12345678");
        e.setDireccion("Calle Test 1");
        e.setCodigoPostal("08001");
        e.setCiudad("Barcelona");
        e.setProvincia("Barcelona");
        e.setPais("España");
        e.setTelefono("612345678");
        e.setEmail("empresa" + id + "@test.com");
        e.setActivo(true);
        return e;
    }

    private static Usuario usuario(Long id, TipoRol rol, Empresa empresa) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre("Usuario " + id);
        u.setEmail("u" + id + "@test.com");
        u.setRol(rol);
        u.setEmpresa(empresa);
        return u;
    }

    private static Cliente clienteParticular(Long id, Empresa empresa) {
        Cliente c = new Cliente();
        c.setId(id);
        c.setTipoCliente(TipoCliente.PARTICULAR);
        c.setNombre("Ana");
        c.setPrimerApellido("Pérez");
        c.setDni("12345678Z");
        c.setEmail("ana@test.com");
        c.setEmpresa(empresa);
        return c;
    }

    private static Cliente clienteEmpresa(Long id, Empresa empresa) {
        Cliente c = new Cliente();
        c.setId(id);
        c.setTipoCliente(TipoCliente.EMPRESA);
        c.setRazonSocial("Cliente SL");
        c.setNif("B12345678");
        c.setEmail("clienteempresa@test.com");
        c.setEmpresa(empresa);
        return c;
    }

    private static ProductoServicio producto(Long id, Empresa empresa) {
        ProductoServicio p = new ProductoServicio();
        p.setId(id);
        p.setTipo(TipoProductoServicio.SERVICIO);
        p.setNombre("Servicio");
        p.setDescripcion("Descripción");
        p.setPrecioBase(new BigDecimal("100"));
        p.setTipoIva(new BigDecimal("21"));
        p.setEmpresa(empresa);
        p.setActivo(true);
        return p;
    }

    private static Invitacion invitacion(Long id, String email, TipoRol rol, Empresa empresa,
                                         EstadoInvitacion estado, LocalDateTime creacion,
                                         LocalDateTime expiracion) {
        Invitacion i = new Invitacion();
        i.setId(id);
        i.setEmail(email);
        i.setRol(rol);
        i.setEmpresa(empresa);
        i.setEstadoInvitacion(estado);
        i.setFechaCreacion(creacion);
        i.setFechaExpiracion(expiracion);
        i.setFechaUltimoEnvio(creacion);
        i.setToken("token-" + id);
        return i;
    }

    private static Presupuesto presupuesto(Long id, Empresa empresa, Cliente cliente,
                                           Usuario usuario, EstadoPresupuesto estado) {
        Presupuesto p = new Presupuesto();
        p.setId(id);
        p.setTitulo("Presupuesto " + id);
        p.setNumeroPresupuesto("PRE-" + LocalDate.now().getYear() + "-0001");
        p.setEstado(estado);
        p.setFechaCreacion(LocalDate.now());
        p.setEmpresa(empresa);
        p.setCliente(cliente);
        p.setUsuario(usuario);
        return p;
    }

    private static ClienteRequestDTO dtoClienteParticular(Long empresaId) {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setTipoCliente(TipoCliente.PARTICULAR);
        dto.setNombre("Ana");
        dto.setPrimerApellido("Pérez");
        dto.setSegundoApellido("López");
        dto.setDni("12345678Z");
        dto.setDireccion("Calle Cliente 1");
        dto.setTelefono("612345678");
        dto.setEmail("ana@test.com");
        dto.setEmpresaId(empresaId);
        return dto;
    }

    private static ClienteRequestDTO dtoClienteEmpresa(Long empresaId) {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setTipoCliente(TipoCliente.EMPRESA);
        dto.setRazonSocial("Cliente SL");
        dto.setNif("B12345678");
        dto.setDireccion("Calle Cliente 2");
        dto.setTelefono("612345678");
        dto.setEmail("cliente@test.com");
        dto.setEmpresaId(empresaId);
        return dto;
    }

    private static EmpresaRequestDTO dtoEmpresa() {
        EmpresaRequestDTO dto = new EmpresaRequestDTO();
        dto.setRazonSocial("Empresa Test SL");
        dto.setNombreComercial("Empresa Test");
        dto.setNif("B12345678");
        dto.setDireccion("Calle Empresa 1");
        dto.setCodigoPostal("08001");
        dto.setCiudad("Barcelona");
        dto.setProvincia("Barcelona");
        dto.setPais("España");
        dto.setTelefono("612345678");
        dto.setEmail("empresa@test.com");
        dto.setLogoUrl("https://example.com/logo.png");
        return dto;
    }

    private static InvitacionRequestDTO dtoInvitacion(String email, TipoRol rol, Long empresaId) {
        InvitacionRequestDTO dto = new InvitacionRequestDTO();
        dto.setEmail(email);
        dto.setRol(rol);
        dto.setEmpresaId(empresaId);
        return dto;
    }

    private static UsuarioRequestDTO dtoUsuarioInvitacion(String token) {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Nuevo Usuario");
        dto.setPrimerApellido("Apellido");
        dto.setPassword("Password123");
        dto.setTokenInvitacion(token);
        return dto;
    }

    private static PresupuestoRequestDTO dtoPresupuestoManual(Long empresaId, Long clienteId, Long usuarioId) {
        LineaPresupuestoRequestDTO linea = new LineaPresupuestoRequestDTO();
        linea.setNombre("Trabajo manual");
        linea.setDescripcion("Descripción");
        linea.setCantidad(BigDecimal.ONE);
        linea.setPrecioUnitario(new BigDecimal("100"));
        linea.setTipoIva(new BigDecimal("21"));
        linea.setPorcentajeDescuento(BigDecimal.ZERO);
        return dtoPresupuestoBase(empresaId, clienteId, usuarioId, List.of(linea));
    }

    private static PresupuestoRequestDTO dtoPresupuestoBase(Long empresaId, Long clienteId,
                                                             Long usuarioId,
                                                             List<LineaPresupuestoRequestDTO> lineas) {
        PresupuestoRequestDTO dto = new PresupuestoRequestDTO();
        dto.setTitulo("Presupuesto test");
        dto.setNotas("Notas");
        dto.setEmpresaId(empresaId);
        dto.setClienteId(clienteId);
        dto.setUsuarioId(usuarioId);
        dto.setLineas(lineas);
        return dto;
    }

    private static void prepararCreacionBase(PresupuestoFixture f, Empresa e, Cliente c, Usuario u) {
        when(f.clienteRepository.findById(c.getId())).thenReturn(Optional.of(c));
        when(f.empresaRepository.findById(e.getId())).thenReturn(Optional.of(e));
        when(f.usuarioRepository.findById(u.getId())).thenReturn(Optional.of(u));
        when(f.presupuestoRepository.findFirstByEmpresaAndFechaCreacionBetweenOrderByNumeroPresupuestoDesc(
                eq(e), any(LocalDate.class), any(LocalDate.class))).thenReturn(Optional.empty());
    }

    // ============================================================
    // JSON PARA CONTROLLERS
    // ============================================================

    private static String jsonClienteParticular() {
        return """
                {
                  "tipoCliente":"PARTICULAR",
                  "nombre":"Ana",
                  "primerApellido":"Pérez",
                  "dni":"12345678Z",
                  "direccion":"Calle Test 1",
                  "telefono":"612345678",
                  "email":"ana@test.com",
                  "empresaId":1
                }
                """;
    }

    private static String jsonEmpresa() {
        return """
                {
                  "razonSocial":"Empresa Test SL",
                  "nombreComercial":"Empresa Test",
                  "nif":"B12345678",
                  "direccion":"Calle Empresa 1",
                  "codigoPostal":"08001",
                  "ciudad":"Barcelona",
                  "provincia":"Barcelona",
                  "pais":"España",
                  "telefono":"612345678",
                  "email":"empresa@test.com",
                  "logoUrl":"https://example.com/logo.png"
                }
                """;
    }

    private static String jsonInvitacion() {
        return """
                {
                  "email":"nuevo@test.com",
                  "rol":"USUARIO",
                  "empresaId":1
                }
                """;
    }

    private static String jsonUsuarioAceptacion(boolean incluirEmail) {
        String email = incluirEmail ? "\n  \"email\":\"alta@test.com\"," : "";
        return "{\n" +
                "  \"nombre\":\"Nuevo Usuario\"," + email + "\n" +
                "  \"password\":\"Password123\",\n" +
                "  \"tokenInvitacion\":\"token-ok\"\n" +
                "}";
    }

    private static String jsonPresupuesto() {
        return """
                {
                  "titulo":"Presupuesto test",
                  "notas":"Notas",
                  "usuarioId":20,
                  "empresaId":1,
                  "clienteId":10,
                  "lineas":[
                    {
                      "nombre":"Trabajo manual",
                      "descripcion":"Descripción",
                      "cantidad":1,
                      "precioUnitario":100,
                      "tipoIva":21,
                      "porcentajeDescuento":0
                    }
                  ]
                }
                """;
    }

    // ============================================================
    // MOTOR DE EJECUCIÓN + INFORME
    // ============================================================

    private MockMvc mockMvc(Object controller) {
        return MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private static int status(org.springframework.test.web.servlet.ResultActions actions) throws Exception {
        return actions.andReturn().getResponse().getStatus();
    }

    private void casoOk(Modulo modulo, Capa capa, String nombre, ThrowingRunnable action) {
        informe.ejecutar(modulo, capa, nombre, null, action);
    }

    private void casoEx(Modulo modulo, Capa capa, String nombre,
                        Class<? extends Throwable> esperada, ThrowingRunnable action) {
        informe.ejecutar(modulo, capa, nombre, esperada, action);
    }

    private void casoHttp(Modulo modulo, String nombre, int esperado, IntSupplierThrowing supplier) {
        informe.ejecutar(modulo, Capa.CONTROLLER, nombre, null, () -> {
            int actual = supplier.getAsInt();
            check(actual == esperado, "HTTP esperado=" + esperado + ", actual=" + actual);
        });
    }

    private static void check(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    private interface IntSupplierThrowing {
        int getAsInt() throws Exception;
    }

    private enum Modulo {
        CLIENTE, EMPRESA, INVITACION, PRESUPUESTO
    }

    private enum Capa {
        SERVICE, CONTROLLER
    }

    private static class Informe {
        private final Map<Modulo, DatosModulo> datos = new EnumMap<>(Modulo.class);

        Informe() {
            for (Modulo modulo : Modulo.values()) datos.put(modulo, new DatosModulo());
        }

        void ejecutar(Modulo modulo, Capa capa, String nombre,
                      Class<? extends Throwable> esperada, ThrowingRunnable action) {
            DatosModulo d = datos.get(modulo);
            d.intentos++;
            d.intentosPorCapa.merge(capa, 1, Integer::sum);

            try {
                action.run();
                if (esperada != null) {
                    d.fallos++;
                    d.fallosDetalle.add("[" + capa + "] " + nombre
                            + " -> NO lanzó " + esperada.getSimpleName());
                } else {
                    d.correctos++;
                }
            } catch (Throwable throwable) {
                Throwable real = causaReal(throwable);

                if (real instanceof AssertionError) {
                    d.fallos++;
                    d.fallosDetalle.add("[" + capa + "] " + nombre + " -> " + real.getMessage());
                    return;
                }

                boolean controlada = esControlada(real);
                if (controlada) {
                    d.excepcionesControladas.merge(real.getClass().getSimpleName(), 1, Integer::sum);
                    d.detalleControladas.add("[" + capa + "] " + nombre + " -> "
                            + real.getClass().getSimpleName()
                            + (esperada != null && esperada.isInstance(real) ? " [esperada]" : " [inesperada]"));
                } else {
                    d.excepcionesNoControladas.merge(real.getClass().getSimpleName(), 1, Integer::sum);
                    d.detalleNoControladas.add("[" + capa + "] " + nombre + " -> "
                            + real.getClass().getName() + ": " + real.getMessage());
                }

                if (esperada != null && esperada.isInstance(real)) {
                    d.correctos++;
                } else {
                    d.fallos++;
                    d.fallosDetalle.add("[" + capa + "] " + nombre + " -> lanzó "
                            + real.getClass().getSimpleName()
                            + (esperada == null ? " y no se esperaba excepción"
                            : ", se esperaba " + esperada.getSimpleName()));
                }
            }
        }

        int totalFallos() {
            return datos.values().stream().mapToInt(d -> d.fallos).sum();
        }

        void imprimir() {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("             INFORME DE PRUEBAS DEL BACKEND");
            System.out.println("============================================================");

            int totalIntentos = 0;
            int totalCorrectos = 0;
            int totalFallos = 0;
            int totalControladas = 0;
            int totalNoControladas = 0;

            for (Modulo modulo : Modulo.values()) {
                DatosModulo d = datos.get(modulo);
                totalIntentos += d.intentos;
                totalCorrectos += d.correctos;
                totalFallos += d.fallos;
                totalControladas += suma(d.excepcionesControladas);
                totalNoControladas += suma(d.excepcionesNoControladas);

                System.out.println();
                System.out.println("---------------- " + modulo + " ----------------");
                System.out.println("Intentos totales: " + d.intentos);
                System.out.println("  Service:        " + d.intentosPorCapa.getOrDefault(Capa.SERVICE, 0));
                System.out.println("  Controller:     " + d.intentosPorCapa.getOrDefault(Capa.CONTROLLER, 0));
                System.out.println("Correctos:        " + d.correctos);
                System.out.println("Fallidos:         " + d.fallos);
                System.out.println("Excepciones:      "
                        + (suma(d.excepcionesControladas) + suma(d.excepcionesNoControladas)));

                System.out.println("Excepciones controladas:");
                if (d.excepcionesControladas.isEmpty()) {
                    System.out.println("  - Ninguna");
                } else {
                    d.excepcionesControladas.forEach((k, v) -> System.out.println("  - " + k + ": " + v));
                    d.detalleControladas.forEach(s -> System.out.println("      " + s));
                }

                System.out.println("Excepciones NO controladas:");
                if (d.excepcionesNoControladas.isEmpty()) {
                    System.out.println("  - Ninguna");
                } else {
                    d.excepcionesNoControladas.forEach((k, v) -> System.out.println("  - " + k + ": " + v));
                    d.detalleNoControladas.forEach(s -> System.out.println("      " + s));
                }

                if (!d.fallosDetalle.isEmpty()) {
                    System.out.println("Fallos detectados:");
                    d.fallosDetalle.forEach(s -> System.out.println("  - " + s));
                }
            }

            System.out.println();
            System.out.println("===================== TOTAL GENERAL =====================");
            System.out.println("Intentos:                  " + totalIntentos);
            System.out.println("Correctos:                 " + totalCorrectos);
            System.out.println("Fallidos:                  " + totalFallos);
            System.out.println("Excepciones controladas:   " + totalControladas);
            System.out.println("Excepciones no controladas:" + totalNoControladas);
            System.out.println("=========================================================");
            System.out.println();
        }

        private static int suma(Map<String, Integer> mapa) {
            return mapa.values().stream().mapToInt(Integer::intValue).sum();
        }

        private static boolean esControlada(Throwable t) {
            return t instanceof RecursoNoEncontradoException
                    || t instanceof AccesoDenegadoException
                    || t instanceof SolicitudInvalidaException
                    || t instanceof ConflictoException;
        }

        private static Throwable causaReal(Throwable t) {
            Throwable actual = t;
            while (actual.getCause() != null
                    && (actual.getClass().getName().contains("ServletException")
                    || actual.getClass().getName().contains("CompletionException")
                    || actual.getClass().getName().contains("ExecutionException"))) {
                actual = actual.getCause();
            }
            return actual;
        }
    }

    private static class DatosModulo {
        int intentos;
        int correctos;
        int fallos;
        final Map<Capa, Integer> intentosPorCapa = new EnumMap<>(Capa.class);
        final Map<String, Integer> excepcionesControladas = new LinkedHashMap<>();
        final Map<String, Integer> excepcionesNoControladas = new LinkedHashMap<>();
        final List<String> detalleControladas = new ArrayList<>();
        final List<String> detalleNoControladas = new ArrayList<>();
        final List<String> fallosDetalle = new ArrayList<>();
    }
}
