package com.presupuestos.presupuestosBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Establecimiento;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {
    
}