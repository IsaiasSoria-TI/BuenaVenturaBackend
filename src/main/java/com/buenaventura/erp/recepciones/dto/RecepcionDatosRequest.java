package com.buenaventura.erp.recepciones.dto;

public class RecepcionDatosRequest {

    private String guiaRemision;
    private String tipoEnvase;
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
