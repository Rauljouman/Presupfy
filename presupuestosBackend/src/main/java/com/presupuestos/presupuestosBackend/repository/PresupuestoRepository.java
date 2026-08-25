package com.presupuestos.presupuestosBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Presupuesto;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    
    
}  