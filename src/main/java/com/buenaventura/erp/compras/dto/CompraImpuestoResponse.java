package com.buenaventura.erp.compras.dto;

import java.math.BigDecimal;

public class CompraImpuestoResponse {

    private Integer idCompraImpuesto;
    private Integer idImpuesto;
    private String tipoImpuesto;
    private BigDecimal porcentaje;
    private BigDecimal importe;

    public CompraImpuestoResponse() {
    }

    public Integer getIdCompraImpuesto() {
        return idCompraImpuesto;
    }

    public void setIdCompraImpuesto(Integer idCompraImpuesto) {
        this.idCompraImpuesto = idCompraImpuesto;
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

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
}