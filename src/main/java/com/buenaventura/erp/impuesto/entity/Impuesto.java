package com.buenaventura.erp.impuesto.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_impuesto")
public class Impuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdImpuesto")
    private Integer idImpuesto;

    @Column(name = "TipoImpuesto", nullable = false, length = 50)
    private String tipoImpuesto;

    @Column(name = "Valor", nullable = false)
    private Integer valor;

    public Impuesto() {
    }

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}