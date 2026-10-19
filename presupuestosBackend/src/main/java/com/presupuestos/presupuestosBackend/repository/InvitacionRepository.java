package com.presupuestos.presupuestosBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Invitacion;

public interface InvitacionRepository extends JpaRepository<Invitacion,Long>{
    
    Optional<Invitacion> findByToken(String token);
    
    Optional<Invitacion> findByEmailAndEmpresaAndAceptadaFalse(String email, Empresa empresa);
}
