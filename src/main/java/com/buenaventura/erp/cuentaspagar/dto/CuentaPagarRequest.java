package com.buenaventura.erp.cuentaspagar.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class CuentaPagarRequest {

    @NotBlank(message = "El tipo de factura es obligatorio")
    @Pattern(
            regexp = "^(UNICA|MULTIPLE)$",
            message = "El tipo de factura debe ser UNICA o MULTIPLE"
    )
    private String tipoFactura;

    private String numeroFactura;

    private String moneda;

    @NotBlank(message = "El codigo de detraccion/retencion es obligatorio")
    private String codigoDetRet;

    private Boolean manual;

    @Valid
    private List<CuentaPagarRegistroDetalleRequest> detalles;

    public CuentaPagarRequest() {
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
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

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public List<CuentaPagarRegistroDetalleRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CuentaPagarRegistroDetalleRequest> detalles) {
        this.detalles = detalles;
    }

    public static class CuentaPagarRegistroDetalleRequest {

        private Integer idCompras;

        private Integer idRecepciones;

        private String numeroFactura;

        public CuentaPagarRegistroDetalleRequest() {
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
    }
}
