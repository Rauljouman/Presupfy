package com.presupuestos.presupuestosBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    boolean existsByNif(String nif);

    boolean existsByEmail(String email);
}