package com.buenaventura.erp.compras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CompraRequest {

    @NotNull(message = "La condición de pago es obligatoria")
    private Integer idPago;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "La fecha de compra es obligatoria")
    private LocalDateTime fechaCompras;

    @NotBlank(message = "La zona de producción es obligatoria")
    private String zonaProduccion;

    @NotNull(message = "Las hectáreas son obligatorias")
    @DecimalMin(value = "0.00", inclusive = true, message = "Las hectáreas no pueden ser negativas")
    private BigDecimal hectareas;

    @Valid
    @NotEmpty(message = "Debe agregar al menos un artículo")
    private List<CompraDetalleRequest> detalles;

    @Valid
    @NotEmpty(message = "Debe agregar al menos un impuesto")
    private List<CompraImpuestoRequest> impuestos;

    public CompraRequest() {
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public LocalDateTime getFechaCompras() {
        return fechaCompras;
    }

    public void setFechaCompras(LocalDateTime fechaCompras) {
        this.fechaCompras = fechaCompras;
    }

    public String getZonaProduccion() {
        return zonaProduccion;
    }

    public void setZonaProduccion(String zonaProduccion) {
        this.zonaProduccion = zonaProduccion;
    }

    public BigDecimal getHectareas() {
        return hectareas;
    }

    public void setHectareas(BigDecimal hectareas) {
        this.hectareas = hectareas;
    }

    public List<CompraDetalleRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalleRequest> detalles) {
        this.detalles = detalles;
    }

    public List<CompraImpuestoRequest> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<CompraImpuestoRequest> impuestos) {
        this.impuestos = impuestos;
    }
}