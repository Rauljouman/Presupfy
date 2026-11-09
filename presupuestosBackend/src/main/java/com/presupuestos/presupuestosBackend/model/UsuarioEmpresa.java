
package com.presupuestos.presupuestosBackend.model;

import com.presupuestos.presupuestosBackend.enums.TipoRol;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usuario_empresa", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "empresa_id"}))
public class UsuarioEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean superAdmin = false;

    @Column(name = "auth_user_id", unique = true)
    private UUID authUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRol rol;

    public UsuarioEmpresa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoRol getRol() {
        return rol;
    }

    public void setRol(TipoRol rol) {
        this.rol = rol;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public UUID getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(UUID authUserId) {
        this.authUserId = authUserId;
    }
}
