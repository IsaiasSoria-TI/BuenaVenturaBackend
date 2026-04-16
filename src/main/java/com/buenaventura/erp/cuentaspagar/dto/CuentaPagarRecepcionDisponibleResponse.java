package com.buenaventura.erp.cuentaspagar.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuentaPagarRecepcionDisponibleResponse {

    private Integer idRecepciones;
    private LocalDateTime fechaRecepcion;
    private BigDecimal recibido;
    private String estadoRecepcion;

    public CuentaPagarRecepcionDisponibleResponse() {
    }

    public Integer getIdRecepciones() {
        return idRecepciones;
    }

    public void setIdRecepciones(Integer idRecepciones) {
        this.idRecepciones = idRecepciones;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public BigDecimal getRecibido() {
        return recibido;
    }

    public void setRecibido(BigDecimal recibido) {
        this.recibido = recibido;
    }

    public String getEstadoRecepcion() {
        return estadoRecepcion;
    }

    public void setEstadoRecepcion(String estadoRecepcion) {
        this.estadoRecepcion = estadoRecepcion;
    }
}