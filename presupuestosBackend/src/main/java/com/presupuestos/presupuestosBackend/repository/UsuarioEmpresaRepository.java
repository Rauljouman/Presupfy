
package com.presupuestos.presupuestosBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presupuestos.presupuestosBackend.model.Usuario;
import com.presupuestos.presupuestosBackend.model.Empresa;
import com.presupuestos.presupuestosBackend.model.UsuarioEmpresa;

public interface UsuarioEmpresaRepository extends JpaRepository<UsuarioEmpresa, Long> {

    List<UsuarioEmpresa> findByUsuario(Usuario usuario);

    List<UsuarioEmpresa> findByEmpresa(Empresa empresa);

    Optional<UsuarioEmpresa> findByUsuarioAndEmpresa(Usuario usuario, Empresa empresa);

    boolean existsByUsuarioAndEmpresa(Usuario usuario, Empresa empresa);
}
