package com.presupuestos.presupuestosBackend.repository;

import java.time.LocalDate;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.Presupuesto;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    Optional<Presupuesto> findFirstByEmpresaAndFechaCreacionBetweenOrderByNumeroPresupuestoDesc(
            Empresa empresa,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}  