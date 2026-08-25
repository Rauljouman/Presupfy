package com.presupuestos.presupuestosBackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public List<Empresa> listar() {
        return empresaRepository.findAll();
    }

    public Optional<Empresa> buscarPorId(Long id) {
        return empresaRepository.findById(id);
    }

    public Empresa guardar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }
}