package com.buenaventura.erp.proveedores.banco.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_banco_proveedor")
public class BancoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdBancoProveedor")
    private Integer idBancoProveedor;

    @Column(name = "IdBanco")
    private Integer idBanco;

    @Column(name = "IdProveedor")
    private Integer idProveedor;

    @Column(name = "CuentaBancaria")
    private String cuentaBancaria;

    @Column(name = "CuentaInterbancaria")
    private String cuentaInterbancaria;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    public BancoProveedor() {
    }

    public Integer getIdBancoProveedor() {
        return idBancoProveedor;
    }

    public void setIdBancoProveedor(Integer idBancoProveedor) {
        this.idBancoProveedor = idBancoProveedor;
    }

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getCuentaInterbancaria() {
        return cuentaInterbancaria;
    }

    public void setCuentaInterbancaria(String cuentaInterbancaria) {
        this.cuentaInterbancaria = cuentaInterbancaria;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }
}