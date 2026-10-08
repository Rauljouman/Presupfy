package com.presupuestos.presupuestosBackend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.dto.LineaPresupuestoRequestDTO;
import com.presupuestos.presupuestosBackend.dto.PresupuestoRequestDTO;
import com.presupuestos.presupuestosBackend.dto.PresupuestoResponseDTO;
import com.presupuestos.presupuestosBackend.enums.EstadoPresupuesto;
import com.presupuestos.presupuestosBackend.exception.ConflictoException;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.exception.SolicitudInvalidaException;
import com.presupuestos.presupuestosBackend.mapper.PresupuestoMapper;
import com.presupuestos.presupuestosBackend.model.Cliente;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.LineaPresupuesto;
import com.presupuestos.presupuestosBackend.model.Presupuesto;
import com.presupuestos.presupuestosBackend.model.ProductoServicio;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.ClienteRepository;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.PresupuestoRepository;
import com.presupuestos.presupuestosBackend.repository.ProductoServicioRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PresupuestoMapper presupuestoMapper;
    private final ProductoServicioRepository productoServicioRepository;

    public PresupuestoService(ProductoServicioRepository productoServicioRepository, PresupuestoMapper presupuestoMapper, UsuarioRepository usuarioRepository, PresupuestoRepository presupuestoRepository, ClienteRepository clienteRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.presupuestoRepository = presupuestoRepository;
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.presupuestoMapper = presupuestoMapper;
        this.productoServicioRepository = productoServicioRepository;
    }

    public List<PresupuestoResponseDTO> listarPresupuesto(Long empresaId){

        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaId);

        if(empresaEncontrada.isEmpty()){
            throw new RecursoNoEncontradoException("No se ha encontrado la empresa");
        }

        Empresa empresa = empresaEncontrada.get();

        List<Presupuesto> presupuestos = presupuestoRepository.findByEmpresa(empresa);

        List<PresupuestoResponseDTO> presupuestosDTO = new ArrayList<>();

        for(Presupuesto presupuesto : presupuestos){
            presupuestosDTO.add(presupuestoMapper.toResponseDTO(presupuesto));
        }

        return presupuestosDTO;
    }

    public PresupuestoResponseDTO crearPresupuesto(PresupuestoRequestDTO dto){
        
        Presupuesto presupuesto = new Presupuesto();

        Long clienteId = dto.getClienteId();
        Optional<Cliente> clienteEncontrado = clienteRepository.findById(clienteId);

        if(clienteEncontrado.isEmpty()){
            throw new RecursoNoEncontradoException("Error, no existe el cliente"); 
        }

        Cliente cliente = clienteEncontrado.get();


        Long empresaId = dto.getEmpresaId();
        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaId);

        if(empresaEncontrada.isEmpty()){
            throw new RecursoNoEncontradoException("Error, no existe la empresa"); 
        }

        Empresa empresa = empresaEncontrada.get();

        if(!cliente.getEmpresa().getId().equals(empresa.getId())) {
            throw new AccesoDenegadoException("Error, el cliente no pertenece a esta empresa"); 
        }

        Long usuarioId = dto.getUsuarioId();
        Optional<Usuario> usuarioEncontrado =  usuarioRepository.findById(usuarioId);

        if(usuarioEncontrado.isEmpty()){
            throw new RecursoNoEncontradoException("Error, usuario no encontrado");
        }

        Usuario usuario = usuarioEncontrado.get();

        if(!usuario.getEmpresa().getId().equals(empresa.getId())){
            throw new AccesoDenegadoException("Error, el usuario no pertenece a esta empresa");
        }   
        
        int anio = LocalDate.now().getYear();
        LocalDate fechaInicio = LocalDate.of(anio,1,1);
        LocalDate fechaFin = LocalDate.of(anio, 12,31);

        Optional<Presupuesto> ultimoPresupuesto = presupuestoRepository.findFirstByEmpresaAndFechaCreacionBetweenOrderByNumeroPresupuestoDesc(empresa, fechaInicio, fechaFin);

        int siguienteNumero;

        if(ultimoPresupuesto.isEmpty()){
            siguienteNumero = 1;

        } else {
            Presupuesto ultimo = ultimoPresupuesto.get();

            String numeroAnterior = ultimo.getNumeroPresupuesto();

            String[] partes = numeroAnterior.split("-");

            String numero = partes[2];

            int numeroConvertido = Integer.parseInt(numero);

            siguienteNumero = numeroConvertido + 1;

        }

        String numeroFormateado = String.format("%04d", siguienteNumero);
        String numeroPresupuesto = "PRE-" + anio + "-" + numeroFormateado;

        List<LineaPresupuestoRequestDTO> lineasDTO = dto.getLineas();

        if (lineasDTO == null || lineasDTO.isEmpty()) {
            throw new SolicitudInvalidaException("El presupuesto debe contener al menos una línea");
        }

        for(LineaPresupuestoRequestDTO lineaDTO : lineasDTO){

            if(lineaDTO.getProductoServicioId() != null){

                //Procutos y servicios del catálgo

                Long productoServicioId = lineaDTO.getProductoServicioId();

                Optional<ProductoServicio> productoEncontrado = productoServicioRepository.findById(productoServicioId);

                if(productoEncontrado.isEmpty()){
                    throw new RecursoNoEncontradoException("Error, no se ha encontrado el producto o servicio.");
                }

                ProductoServicio productoServicio = productoEncontrado.get();

                if(!productoServicio.getEmpresa().getId().equals(empresa.getId())){
                    throw new AccesoDenegadoException("Error, el producto o servicio no pertenece a esta empresa.");

                }

                LineaPresupuesto linea = new LineaPresupuesto();

                linea.setProductoServicio(productoServicio);
                linea.setNombre(productoServicio.getNombre());
                linea.setDescripcion(productoServicio.getDescripcion());
                linea.setPrecioUnitario(productoServicio.getPrecioBase());
                linea.setTipoIva(productoServicio.getTipoIva());
                linea.setCantidad(lineaDTO.getCantidad());
                linea.setPorcentajeDescuento(lineaDTO.getPorcentajeDescuento());
                linea.setPresupuesto(presupuesto);

                presupuesto.getLineas().add(linea);
            }
            else {

                if(lineaDTO.getNombre() == null || lineaDTO.getTipoIva() == null || lineaDTO.getPrecioUnitario() == null || lineaDTO.getNombre().isBlank()){

                    throw new SolicitudInvalidaException("Una línea manual necesita nombre, precio e IVA");

                }
                
                //Productos y servicios en modo manual

                LineaPresupuesto linea = new LineaPresupuesto();

                linea.setNombre(lineaDTO.getNombre());
                linea.setDescripcion(lineaDTO.getDescripcion());
                linea.setPrecioUnitario(lineaDTO.getPrecioUnitario());
                linea.setTipoIva(lineaDTO.getTipoIva());

                linea.setCantidad(lineaDTO.getCantidad());
                linea.setPorcentajeDescuento(lineaDTO.getPorcentajeDescuento());

                linea.setPresupuesto(presupuesto);

                presupuesto.getLineas().add(linea);
            }
        }

        presupuesto.setUsuario(usuario);
        presupuesto.setCliente(cliente);
        presupuesto.setEstado(EstadoPresupuesto.BORRADOR);
        presupuesto.setFechaCreacion(LocalDate.now());
        presupuesto.setEmpresa(empresa);
        presupuesto.setNumeroPresupuesto(numeroPresupuesto);
        presupuesto.setTitulo(dto.getTitulo());
        presupuesto.setNotas(dto.getNotas());   

        Presupuesto presupuestoGuardado = presupuestoRepository.save(presupuesto);

        return presupuestoMapper.toResponseDTO(presupuestoGuardado);
    }

    public PresupuestoResponseDTO enviarPresupuesto(Long id){

        Optional<Presupuesto> presupuestoEncontrado = presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RecursoNoEncontradoException("No se ha encontrado el presupuesto");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if(presupuesto.getEstado() != EstadoPresupuesto.BORRADOR){
            throw new ConflictoException("El presupuesto solo se puede enviar en estado borrador");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        }

        presupuesto.setEstado(EstadoPresupuesto.ENVIADO);
        presupuesto.setFechaEnvio(LocalDate.now());

        Presupuesto presupuestoEnviado = presupuestoRepository.save(presupuesto);

        return presupuestoMapper.toResponseDTO(presupuestoEnviado);
    }

    public PresupuestoResponseDTO aprobarPresupuesto(Long id){

        Optional<Presupuesto> presupuestoEncontrado = presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RecursoNoEncontradoException("Error, no se ha encontrado el presupuesto");
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if(presupuesto.getEstado() != EstadoPresupuesto.ENVIADO){
            throw new ConflictoException("Solo se pueden aprobar presupuestos enviados");
        }

        presupuesto.setEstado(EstadoPresupuesto.APROBADO);
        presupuesto.setFechaRespuesta(LocalDate.now());

        Presupuesto presupuestoAprobado = presupuestoRepository.save(presupuesto);

        return presupuestoMapper.toResponseDTO(presupuestoAprobado);
    }

    public PresupuestoResponseDTO rechazarPresupuesto(Long id){

        Optional<Presupuesto> presupuestoEncontrado = presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RecursoNoEncontradoException("No se ha encontrado el presupuesto");
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if (presupuesto.getEstado() != EstadoPresupuesto.ENVIADO) {
            throw new ConflictoException("Solo se pueden rechazar presupuestos enviados");
        }

        presupuesto.setEstado(EstadoPresupuesto.RECHAZADO);
        presupuesto.setFechaRespuesta(LocalDate.now());

        Presupuesto presupuestoRechazado = presupuestoRepository.save(presupuesto);

        return presupuestoMapper.toResponseDTO(presupuestoRechazado);
    }

    public PresupuestoResponseDTO caducarPresupuesto(Long id){
        Optional<Presupuesto> presupuestoEncontrado = presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RecursoNoEncontradoException("No se encontro el presupuesto");
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if(presupuesto.getEstado() != EstadoPresupuesto.ENVIADO){
            throw new ConflictoException("Solo se pueden caducar presupuestos enviados");
        }

        presupuesto.setEstado(EstadoPresupuesto.CADUCADO);

        Presupuesto presupuestoCaducado = presupuestoRepository.save(presupuesto);

        return presupuestoMapper.toResponseDTO(presupuestoCaducado);
    }

    public boolean necesitaAvisoRespuesta(Presupuesto presupuesto){

        if(presupuesto.getEstado() != EstadoPresupuesto.ENVIADO){
            return false;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = presupuesto.getFechaEnvio().plusDays(15);

        if(hoy.isEqual(fechaLimite) || hoy.isAfter(fechaLimite)){
            return true;
        }
        
        return false;
    }

    public void eliminarPresupuesto(Long id){
        Optional<Presupuesto> presupuestoEncontrado =  presupuestoRepository.findById(id);

        if(presupuestoEncontrado.isEmpty()){
            throw new RecursoNoEncontradoException("El presupuesto no existe");
        }

        Presupuesto presupuesto = presupuestoEncontrado.get();

        if(presupuesto.getEstado() != EstadoPresupuesto.BORRADOR){
            throw new ConflictoException("El presupuesto solo puede borrarse en estado 'Borrador'");
        }

        presupuestoRepository.delete(presupuesto);
    }

}