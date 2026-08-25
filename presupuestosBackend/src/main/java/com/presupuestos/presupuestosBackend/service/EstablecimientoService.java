package com.presupuestos.presupuestosBackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.model.Establecimiento;
import com.presupuestos.presupuestosBackend.repository.EstablecimientoRepository;

@Service
public class EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;

    public EstablecimientoService(EstablecimientoRepository establecimientoRepository) {
        this.establecimientoRepository = establecimientoRepository;
    }

    public List<Establecimiento> listar() {
        return establecimientoRepository.findAll();
    }

    public Optional<Establecimiento> buscarPorId(Long id) {
        return establecimientoRepository.findById(id);
    }

    public Establecimiento guardar(Establecimiento establecimiento) {
        return establecimientoRepository.save(establecimiento);
    }
}