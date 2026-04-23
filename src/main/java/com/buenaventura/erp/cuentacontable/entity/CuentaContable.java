package com.buenaventura.erp.cuentacontable.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_cuenta_contable")
public class CuentaContable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCuentaContable")
    private Integer idCuentaContable;

    @Column(name = "Codigo", nullable = false, length = 20)
    private String codigo;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    public CuentaContable() {
    }

    public Integer getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(Integer idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}