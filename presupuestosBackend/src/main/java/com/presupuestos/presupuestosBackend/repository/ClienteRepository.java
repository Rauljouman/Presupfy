package com.presupuestos.presupuestosBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Cliente;
import com.presupuestos.presupuestosBackend.model.Empresa;
import java.util.*;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    List<Cliente> findByEmpresa(Empresa empresa);
}