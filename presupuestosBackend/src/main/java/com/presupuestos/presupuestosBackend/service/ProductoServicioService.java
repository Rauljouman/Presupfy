
package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import com.presupuestos.presupuestosBackend.dto.ProductoServicioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.ProductoServicioResponseDTO;
import com.presupuestos.presupuestosBackend.enums.TipoRol;
import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;
import com.presupuestos.presupuestosBackend.exception.ConflictoException;
import com.presupuestos.presupuestosBackend.exception.RecursoNoEncontradoException;
import com.presupuestos.presupuestosBackend.mapper.ProductoServicioMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.ProductoServicio;
import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.repository.ProductoServicioRepository;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;
import com.presupuestos.presupuestosBackend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductoServicioService {

    private final ProductoServicioRepository productoServicioRepository;
    private final EmpresaRepository empresaRepository;
    private final ProductoServicioMapper productoServicioMapper;
    private final UsuarioRepository usuarioRepository;

    public ProductoServicioService(ProductoServicioRepository productoServicioRepository, ProductoServicioMapper productoServicioMapper, EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository) {
        this.productoServicioRepository = productoServicioRepository;
        this.empresaRepository = empresaRepository;
        this.productoServicioMapper = productoServicioMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ProductoServicioResponseDTO> listarProductoServicio(Long empresaId, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        Empresa empresa = buscarEmpresa(empresaId);

        comprobarAcceso(usuario, empresa);

        List<ProductoServicio> productos = productoServicioRepository.findByEmpresa(empresa);
        List<ProductoServicioResponseDTO> listaDTO = new ArrayList<>();

        for(ProductoServicio producto : productos){
            listaDTO.add(productoServicioMapper.toResponseDTO(producto));
        }

        return listaDTO;
    }

    public ProductoServicioResponseDTO buscarProductoServicioId(Long id, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        ProductoServicio producto = productoServicioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto o servicio"));

        comprobarAcceso(usuario, producto.getEmpresa());

        return productoServicioMapper.toResponseDTO(producto);
    }

    @Transactional
    public ProductoServicioResponseDTO crearProductoServicio(ProductoServicioRequestDTO dto, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        Empresa empresa = buscarEmpresa(dto.getEmpresaId());

        comprobarPermisosEdicion(usuario, empresa);

        if(!empresa.isActivo()){
            throw new ConflictoException("La empresa está desactivada");
        }

        ProductoServicio productoServicio = new ProductoServicio();

        productoServicio.setTipo(dto.getTipo());
        productoServicio.setNombre(dto.getNombre());
        productoServicio.setDescripcion(dto.getDescripcion());
        productoServicio.setPrecioBase(dto.getPrecioBase());
        productoServicio.setTipoIva(dto.getTipoIva());
        productoServicio.setEmpresa(empresa);
        productoServicio.setActivo(true);

        ProductoServicio productoServicioGuardado = productoServicioRepository.save(productoServicio);

        return productoServicioMapper.toResponseDTO(productoServicioGuardado);
    }

    @Transactional
    public ProductoServicioResponseDTO actualizarProductoServicio(Long id, ProductoServicioRequestDTO dto, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        ProductoServicio producto = productoServicioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto o servicio"));

        comprobarPermisosEdicion(usuario, producto.getEmpresa());

        if(!producto.getEmpresa().isActivo()){
            throw new ConflictoException("La empresa está desactivada");
        }

        if(!producto.isActivo()){
            throw new ConflictoException("El producto o servicio está desactivado");
        }

        if(!producto.getEmpresa().getId().equals(dto.getEmpresaId())){
            throw new ConflictoException("No se puede cambiar la empresa de un producto o servicio");
        }

        producto.setTipo(dto.getTipo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioBase(dto.getPrecioBase());
        producto.setTipoIva(dto.getTipoIva());

        return productoServicioMapper.toResponseDTO(producto);
    }

    @Transactional
    public void desactivarProductoServicio(Long id, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        ProductoServicio producto = productoServicioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto o servicio"));

        comprobarPermisosEdicion(usuario, producto.getEmpresa());

        if(!producto.isActivo()){
            throw new ConflictoException("El producto o servicio ya está desactivado");
        }

        producto.setActivo(false);
    }

    @Transactional
    public void reactivarProductoServicio(Long id, Long usuarioId){

        Usuario usuario = buscarUsuario(usuarioId);
        ProductoServicio producto = productoServicioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto o servicio"));

        comprobarPermisosEdicion(usuario, producto.getEmpresa());

        if(!producto.getEmpresa().isActivo()){
            throw new ConflictoException("La empresa está desactivada");
        }

        if(producto.isActivo()){
            throw new ConflictoException("El producto o servicio ya está activo");
        }

        producto.setActivo(true);
    }

    private Usuario buscarUsuario(Long usuarioId){
        return usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado al usuario"));
    }

    private Empresa buscarEmpresa(Long empresaId){
        return empresaRepository.findById(empresaId).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la empresa"));
    }

    private void comprobarAcceso(Usuario usuario, Empresa empresa){

        if(usuario.getRol() == TipoRol.SUPERADMIN){
            return;
        }

        if(usuario.getEmpresa() == null || !usuario.getEmpresa().getId().equals(empresa.getId())){
            throw new AccesoDenegadoException("No puedes acceder a los productos o servicios de otra empresa");
        }
    }

    private void comprobarPermisosEdicion(Usuario usuario, Empresa empresa){

        if(usuario.getRol() == TipoRol.USUARIO){
            throw new AccesoDenegadoException("No tienes permisos suficientes para modificar productos o servicios");
        }

        comprobarAcceso(usuario, empresa);
    }
}
