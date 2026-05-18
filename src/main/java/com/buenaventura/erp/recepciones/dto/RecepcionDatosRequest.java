package com.buenaventura.erp.recepciones.dto;

import java.math.BigDecimal;

public class RecepcionDatosRequest {

    private String guiaRemision;
    private BigDecimal cantidadJabas;

    public String getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(String guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public BigDecimal getCantidadJabas() {
        return cantidadJabas;
    }

    public void setCantidadJabas(BigDecimal cantidadJabas) {
        this.cantidadJabas = cantidadJabas;
    }
}
