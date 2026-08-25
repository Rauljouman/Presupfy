package com.presupuestos.presupuestosBackend.repository;

import com.presupuestos.presupuestosBackend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}