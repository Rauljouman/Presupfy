package com.presupuestos.presupuestosBackend.repository;

import com.presupuestos.presupuestosBackend.model.Establecimiento;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {
    
}