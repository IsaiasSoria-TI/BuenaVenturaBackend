package com.buenaventura.erp.inventario.tipoenvase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_tipo_envase")
public class TipoEnvase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_envase")
    private Integer idTipoEnvase;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    public TipoEnvase() {
    }

    public Integer getIdTipoEnvase() {
        return idTipoEnvase;
    }

    public void setIdTipoEnvase(Integer idTipoEnvase) {
        this.idTipoEnvase = idTipoEnvase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
