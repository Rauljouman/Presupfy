package com.presupuestos.presupuestosBackend.service;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.repository.EmpresaRepository;


@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public List<Empresa> listarEmpresa(){
        return this.empresaRepository.findAll();
    }

    public Optional<Empresa> buscarEmpresaId(Long id){
        return this.empresaRepository.findById(id);
    }

    public Empresa crearEmpresa(Empresa empresa){
        return empresaRepository.save(empresa);
    }

    public Empresa actualizarEmpresa(Empresa empresa){
        return empresaRepository.save(empresa);
    }

    public void eliminarEmpresa(Long id){
        empresaRepository.deleteById(id);
    }
}