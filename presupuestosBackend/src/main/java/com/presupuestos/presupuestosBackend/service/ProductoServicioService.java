package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

import com.presupuestos.presupuestosBackend.model.ProductoServicio;
import com.presupuestos.presupuestosBackend.repository.ProductoServicioRepository;

@Service
public class ProductoServicioService {

    private final ProductoServicioRepository productoServicioRepository;

    public ProductoServicioService(ProductoServicioRepository productoServicioRepository) {
        this.productoServicioRepository = productoServicioRepository;
    }
    
    public List<ProductoServicio> listarProductoServicio(){
        return productoServicioRepository.findAll();
    }

    public Optional<ProductoServicio> buscarProductoServicioId(Long id){
        return productoServicioRepository.findById(id);
    }

    public ProductoServicio creaProductoServicio(ProductoServicio productoServicio){
        return productoServicioRepository.save(productoServicio);
    }

    public ProductoServicio actualizarServicio(ProductoServicio productoServicio){
        return productoServicioRepository.save(productoServicio);
    }

    public void eliminarProductoServicio(Long id){
        productoServicioRepository.deleteById(id);
    }
}