package com.buenaventura.erp.proveedores.banco.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_banco")
public class Banco {

    @Id
    @Column(name = "IdBanco")
    private Integer idBanco;

    @Column(name = "Banco", nullable = false, length = 50)
    private String nombre;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    public Banco() {
    }

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }
}