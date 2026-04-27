package com.buenaventura.erp.cuentaspagar.dto;

import java.math.BigDecimal;

public class CuentaPagarDetalleResponse {

    private Integer idCuentaPagarDetalle;
    private Integer idRecepcionDetalle;
    private Integer idArticulo;
    private String articulo;
    private String medida;
    private BigDecimal recibido;
    private BigDecimal costoKilo;
    private BigDecimal importe;
    private String estado;

    public Integer getIdCuentaPagarDetalle() {
        return idCuentaPagarDetalle;
    }

    public void setIdCuentaPagarDetalle(Integer idCuentaPagarDetalle) {
        this.idCuentaPagarDetalle = idCuentaPagarDetalle;
    }

    public Integer getIdRecepcionDetalle() {
        return idRecepcionDetalle;
    }

    public void setIdRecepcionDetalle(Integer idRecepcionDetalle) {
        this.idRecepcionDetalle = idRecepcionDetalle;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public BigDecimal getRecibido() {
        return recibido;
    }

    public void setRecibido(BigDecimal recibido) {
        this.recibido = recibido;
    }

    public BigDecimal getCostoKilo() {
        return costoKilo;
    }

    public void setCostoKilo(BigDecimal costoKilo) {
        this.costoKilo = costoKilo;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}