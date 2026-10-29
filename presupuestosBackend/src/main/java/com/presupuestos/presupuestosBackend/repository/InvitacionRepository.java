package com.presupuestos.presupuestosBackend.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.enums.EstadoInvitacion;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Invitacion;

public interface InvitacionRepository extends JpaRepository<Invitacion,Long>{
    
    Optional<Invitacion> findByToken(String token);
    
    Optional<Invitacion> findByEmailAndEmpresaAndEstadoInvitacion(String email, Empresa empresa, EstadoInvitacion estadoInvitacion);

    List<Invitacion> findByEstadoInvitacionAndFechaExpiracionLessThanEqual(EstadoInvitacion estadoInvitacion, LocalDateTime localDateTime);
}