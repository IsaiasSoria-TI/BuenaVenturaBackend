package com.buenaventura.erp.compras.entity;

import com.buenaventura.erp.articulo.entity.Articulo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_compras_detalle")
public class CompraDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCompraDetalle")
    private Integer idCompraDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCompras", nullable = false)
    private Compra compra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdArticulo", nullable = false)
    private Articulo articulo;

    @Column(name = "Peso", nullable = false, precision = 10, scale = 2)
    private BigDecimal peso;

    @Column(name = "CostoKilo", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoKilo;

    @Column(name = "CostoTotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaActualizacion")
    private LocalDateTime fechaActualizacion;

    public CompraDetalle() {
    }

    public Integer getIdCompraDetalle() {
        return idCompraDetalle;
    }

    public void setIdCompraDetalle(Integer idCompraDetalle) {
        this.idCompraDetalle = idCompraDetalle;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
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