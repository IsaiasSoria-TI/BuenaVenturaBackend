package com.buenaventura.erp.compras.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CompraResponse {

    private Integer idCompras;

    private Integer idPago;
    private String pago;

    private Integer idMoneda;
    private String codigoMoneda;
    private String moneda;
    private String simboloMoneda;
    private Integer idTipoCambio;
    private BigDecimal tipoCambioAplicado;

    private Integer idProveedor;
    private String ruc;
    private String razonSocial;
    private String direccion;

    private LocalDateTime fechaCompras;
    private LocalDateTime fechaEmision;
    private LocalDateTime fechaIngresoProducto;
    private String tipoDocumento;
    private String numeroDocumentoProveedor;
    private String serieReferencia;
    private String correlativoReferencia;
    private String observacion;
    private String zonaProduccion;
    private BigDecimal numeroLote;

    private BigDecimal peso;
    private BigDecimal costoTotal;
    private BigDecimal importeImpuesto;
    private Boolean aplicaIgv;
    private BigDecimal porcentajeIgv;
    private BigDecimal importeIgv;
    private BigDecimal totalGeneral;

    private String estado;
    private Boolean flgActivo;

    private List<CompraDetalleResponse> detalles;
    private List<CompraImpuestoResponse> impuestos;

    public CompraResponse() {
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public Integer getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(Integer idMoneda) {
        this.idMoneda = idMoneda;
    }

    public String getCodigoMoneda() {
        return codigoMoneda;
    }

    public void setCodigoMoneda(String codigoMoneda) {
        this.codigoMoneda = codigoMoneda;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getSimboloMoneda() {
        return simboloMoneda;
    }

    public void setSimboloMoneda(String simboloMoneda) {
        this.simboloMoneda = simboloMoneda;
    }

    public Integer getIdTipoCambio() {
        return idTipoCambio;
    }

    public void setIdTipoCambio(Integer idTipoCambio) {
        this.idTipoCambio = idTipoCambio;
    }

    public BigDecimal getTipoCambioAplicado() {
        return tipoCambioAplicado;
    }

    public void setTipoCambioAplicado(BigDecimal tipoCambioAplicado) {
        this.tipoCambioAplicado = tipoCambioAplicado;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaCompras() {
        return fechaCompras;
    }

    public void setFechaCompras(LocalDateTime fechaCompras) {
        this.fechaCompras = fechaCompras;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDateTime getFechaIngresoProducto() {
        return fechaIngresoProducto;
    }

    public void setFechaIngresoProducto(LocalDateTime fechaIngresoProducto) {
        this.fechaIngresoProducto = fechaIngresoProducto;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumentoProveedor() {
        return numeroDocumentoProveedor;
    }

    public void setNumeroDocumentoProveedor(String numeroDocumentoProveedor) {
        this.numeroDocumentoProveedor = numeroDocumentoProveedor;
    }

    public String getSerieReferencia() {
        return serieReferencia;
    }

    public void setSerieReferencia(String serieReferencia) {
        this.serieReferencia = serieReferencia;
    }

    public String getCorrelativoReferencia() {
        return correlativoReferencia;
    }

    public void setCorrelativoReferencia(String correlativoReferencia) {
        this.correlativoReferencia = correlativoReferencia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getZonaProduccion() {
        return zonaProduccion;
    }

    public void setZonaProduccion(String zonaProduccion) {
        this.zonaProduccion = zonaProduccion;
    }

    public BigDecimal getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(BigDecimal numeroLote) {
        this.numeroLote = numeroLote;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public BigDecimal getImporteImpuesto() {
        return importeImpuesto;
    }

    public void setImporteImpuesto(BigDecimal importeImpuesto) {
        this.importeImpuesto = importeImpuesto;
    }

    public Boolean getAplicaIgv() {
        return aplicaIgv;
    }

    public void setAplicaIgv(Boolean aplicaIgv) {
        this.aplicaIgv = aplicaIgv;
    }

    public BigDecimal getPorcentajeIgv() {
        return porcentajeIgv;
    }

    public void setPorcentajeIgv(BigDecimal porcentajeIgv) {
        this.porcentajeIgv = porcentajeIgv;
    }

    public BigDecimal getImporteIgv() {
        return importeIgv;
    }

    public void setImporteIgv(BigDecimal importeIgv) {
        this.importeIgv = importeIgv;
    }

    public BigDecimal getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(BigDecimal totalGeneral) {
        this.totalGeneral = totalGeneral;
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

    public List<CompraDetalleResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalleResponse> detalles) {
        this.detalles = detalles;
    }

    public List<CompraImpuestoResponse> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<CompraImpuestoResponse> impuestos) {
        this.impuestos = impuestos;
    }
}
