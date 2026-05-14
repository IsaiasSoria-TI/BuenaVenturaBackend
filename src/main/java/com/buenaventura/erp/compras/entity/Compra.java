package com.buenaventura.erp.compras.entity;

import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.impuesto.entity.Impuesto;
import com.buenaventura.erp.moneda.entity.Moneda;
import com.buenaventura.erp.pago.entity.Pago;
import com.buenaventura.erp.proveedores.entity.Proveedor;
import com.buenaventura.erp.tipocambio.entity.TipoCambio;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCompras")
    private Integer idCompras;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdImpuesto")
    private Impuesto impuesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdPago", nullable = false)
    private Pago pago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_moneda", nullable = false)
    private Moneda moneda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipoCambio")
    private TipoCambio tipoCambio;

    @Column(name = "TipoCambioAplicado", precision = 10, scale = 4)
    private BigDecimal tipoCambioAplicado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdProveedor", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdArticulo")
    private Articulo articulo;

    @Column(name = "FechaCompras")
    private LocalDateTime fechaCompras;

    @Column(name = "ZonaProduccion", nullable = false, length = 100)
    private String zonaProduccion;

    @Column(name = "Numero_lote", nullable = false, precision = 10, scale = 2)
    private BigDecimal numeroLote;

    @Column(name = "Peso", nullable = false, precision = 10, scale = 2)
    private BigDecimal peso;

    @Column(name = "CostoKilo", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoKilo;

    @Column(name = "CostoTotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "ImporteImpuesto", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeImpuesto;

    @Column(name = "AplicaIGV")
    private Boolean aplicaIgv;

    @Column(name = "PorcentajeIGV", precision = 10, scale = 2)
    private BigDecimal porcentajeIgv;

    @Column(name = "ImporteIGV", precision = 10, scale = 2)
    private BigDecimal importeIgv;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaActualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    public Compra() {
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public TipoCambio getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(TipoCambio tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public BigDecimal getTipoCambioAplicado() {
        return tipoCambioAplicado;
    }

    public void setTipoCambioAplicado(BigDecimal tipoCambioAplicado) {
        this.tipoCambioAplicado = tipoCambioAplicado;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
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

    public BigDecimal getCostoKilo() {
        return costoKilo;
    }

    public void setCostoKilo(BigDecimal costoKilo) {
        this.costoKilo = costoKilo;
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

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
