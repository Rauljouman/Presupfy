package com.presupuestos.presupuestosBackend.repository;

import com.presupuestos.presupuestosBackend.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    boolean existsByNif(String nif);

    boolean existsByEmail(String email);
}