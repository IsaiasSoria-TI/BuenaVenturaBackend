package com.buenaventura.erp.pago.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPago")
    private Integer idPago;

    @Column(name = "Pago", nullable = false, length = 50)
    private String pago;

    @Column(name = "Dias")
    private Integer dias;

    public Pago() {
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }
}