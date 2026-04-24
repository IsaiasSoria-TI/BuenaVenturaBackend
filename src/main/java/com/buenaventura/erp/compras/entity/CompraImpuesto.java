package com.buenaventura.erp.compras.entity;

import com.buenaventura.erp.impuesto.entity.Impuesto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_compras_impuesto")
public class CompraImpuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCompraImpuesto")
    private Integer idCompraImpuesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCompras", nullable = false)
    private Compra compra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdImpuesto", nullable = false)
    private Impuesto impuesto;

    @Column(name = "Porcentaje", nullable = false, precision = 10, scale = 2)
    private BigDecimal porcentaje;

    @Column(name = "Importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaActualizacion")
    private LocalDateTime fechaActualizacion;

    public CompraImpuesto() {
    }

    public Integer getIdCompraImpuesto() {
        return idCompraImpuesto;
    }

    public void setIdCompraImpuesto(Integer idCompraImpuesto) {
        this.idCompraImpuesto = idCompraImpuesto;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
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
}