package com.presupuestos.presupuestosBackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.ProductoServicio;

public interface ProductoServicioRepository extends JpaRepository<ProductoServicio, Long> {
    
    List<ProductoServicio> findByEmpresa(Empresa empresa);

}