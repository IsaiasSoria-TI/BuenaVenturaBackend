package com.buenaventura.erp.cuentaspagar.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_cuentas_pagar")
public class CuentaPagar {

    public static final String ESTADO_PENDIENTE = "Pendiente";
    public static final String ESTADO_COMPLETA_PARCIAL = "Completa parcial";
    public static final String ESTADO_COMPLETA = "Completa";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCuentaPagar")
    private Integer idCuentaPagar;

    @Column(name = "IdCompras", nullable = false)
    private Integer idCompras;

    @Column(name = "IdRecepciones", nullable = false)
    private Integer idRecepciones;

    @Column(name = "NumeroFactura", nullable = false, length = 20)
    private String numeroFactura;

    @Column(name = "Moneda", nullable = false, length = 3)
    private String moneda;

    @Column(name = "CodigoDetRet", nullable = false, length = 10)
    private String codigoDetRet;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "FlgActivo", nullable = false)
    private Boolean flgActivo;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaActualizacion")
    private LocalDateTime fechaActualizacion;

    public CuentaPagar() {
    }

    public Integer getIdCuentaPagar() {
        return idCuentaPagar;
    }

    public void setIdCuentaPagar(Integer idCuentaPagar) {
        this.idCuentaPagar = idCuentaPagar;
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public Integer getIdRecepciones() {
        return idRecepciones;
    }

    public void setIdRecepciones(Integer idRecepciones) {
        this.idRecepciones = idRecepciones;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCodigoDetRet() {
        return codigoDetRet;
    }

    public void setCodigoDetRet(String codigoDetRet) {
        this.codigoDetRet = codigoDetRet;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}