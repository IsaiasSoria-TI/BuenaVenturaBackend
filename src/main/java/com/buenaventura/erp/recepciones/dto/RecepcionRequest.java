package com.buenaventura.erp.recepciones.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RecepcionRequest {

    @NotNull(message = "La compra es obligatoria")
    private Integer idCompras;

    private String guiaRemision;

    private String tipoEnvase;

    @Min(value = 0, message = "La cantidad de envase no puede ser negativa")
    private Integer cantidadEnvase;

    @Valid
    @NotEmpty(message = "Debe agregar al menos un detalle de recepción")
    private List<RecepcionDetalleRequest> detalles;

    public RecepcionRequest() {
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

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

    public List<RecepcionDetalleRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RecepcionDetalleRequest> detalles) {
        this.detalles = detalles;
    }
}
