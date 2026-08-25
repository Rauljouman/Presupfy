package com.presupuestos.presupuestosBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.LineaPresupuesto;

public interface LineaPresupuestoRepository extends JpaRepository<LineaPresupuesto, Long> {
    
    
}