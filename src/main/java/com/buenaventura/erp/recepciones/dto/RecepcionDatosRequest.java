package com.buenaventura.erp.recepciones.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class RecepcionDatosRequest {

    @Size(max = 50, message = "La guia de remision no puede superar 50 caracteres")
    private String guiaRemision;

    @Size(max = 50, message = "El tipo de envase no puede superar 50 caracteres")
    private String tipoEnvase;

    @PositiveOrZero(message = "La cantidad de envases no puede ser negativa")
    private Integer cantidadEnvase;

    public String getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(String guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public String getTipoEnvase() {
        return tipoEnvase;
    }

    public void setTipoEnvase(String tipoEnvase) {
        this.tipoEnvase = tipoEnvase;
    }

    public Integer getCantidadEnvase() {
        return cantidadEnvase;
    }

    public void setCantidadEnvase(Integer cantidadEnvase) {
        this.cantidadEnvase = cantidadEnvase;
    }
}
