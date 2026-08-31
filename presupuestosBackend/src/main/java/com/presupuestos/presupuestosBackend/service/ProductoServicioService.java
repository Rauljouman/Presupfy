package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

import com.presupuestos.presupuestosBackend.dto.ProductoServicioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.ProductoServicioResponseDTO;
import com.presupuestos.presupuestosBackend.mapper.ProductoServicioMapper;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.ProductoServicio;
import com.presupuestos.presupuestosBackend.repository.ProductoServicioRepository;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;



@Service
public class ProductoServicioService {

    private final ProductoServicioRepository productoServicioRepository;
    private final EmpresaRepository empresaRepository;
    private final ProductoServicioMapper productoServicioMapper;

    public ProductoServicioService(ProductoServicioRepository productoServicioRepository,ProductoServicioMapper productoServicioMapper, EmpresaRepository empresaRepository) {
        this.productoServicioRepository = productoServicioRepository;
        this.empresaRepository = empresaRepository;
        this.productoServicioMapper = productoServicioMapper;
    }
    
    public List<ProductoServicio> listarProductoServicio(){
        return productoServicioRepository.findAll();
    }

    public Optional<ProductoServicio> buscarProductoServicioId(Long id){
        return productoServicioRepository.findById(id);
    }

    public ProductoServicioResponseDTO crearProductoServicio(ProductoServicioRequestDTO dto){
        
        Long empresaId = dto.getEmpresaId();
        Optional<Empresa> empresaEncontrada = empresaRepository.findById(empresaId);

        if(empresaEncontrada.isEmpty()){
            throw new RuntimeException("Error, no se ha encontrado ninguna empresa.");
        }

        Empresa empresa = empresaEncontrada.get();
        ProductoServicio productoServicio = new ProductoServicio();

        productoServicio.setTipo(dto.getTipo());
        productoServicio.setNombre(dto.getNombre());
        productoServicio.setDescripcion(dto.getDescripcion());
        productoServicio.setPrecioBase(dto.getPrecioBase());
        productoServicio.setTipoIva(dto.getTipoIva());
        productoServicio.setEmpresa(empresa);

        ProductoServicio productoServicioGuardado = productoServicioRepository.save(productoServicio);
        return productoServicioMapper.toResponseDTO(productoServicioGuardado);
    }

    public ProductoServicio actualizarServicio(ProductoServicio productoServicio){
        return productoServicioRepository.save(productoServicio);
    }

    public void eliminarProductoServicio(Long id){
        productoServicioRepository.deleteById(id);
    }
}