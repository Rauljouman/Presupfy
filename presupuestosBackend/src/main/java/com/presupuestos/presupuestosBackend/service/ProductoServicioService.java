package com.presupuestos.presupuestosBackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.model.ProductoServicio;
import com.presupuestos.presupuestosBackend.repository.ProductoServicioRepository;

@Service
public class ProductoServicioService {

    private final ProductoServicioRepository productoServicioRepository;

    public ProductoServicioService(ProductoServicioRepository productoServicioRepository) {
        this.productoServicioRepository = productoServicioRepository;
    }

    public List<ProductoServicio> listar() {
        return productoServicioRepository.findAll();
    }

    public Optional<ProductoServicio> buscarPorId(Long id) {
        return productoServicioRepository.findById(id);
    }

    public ProductoServicio guardar(ProductoServicio productoServicio) {
        return productoServicioRepository.save(productoServicio);
    }
}