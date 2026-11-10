package com.presupuestos.presupuestosBackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    List<Usuario> findByEmpresa(Empresa empresa);

    Optional<Usuario> findByAuthUserId(UUID authUserId);
    
}