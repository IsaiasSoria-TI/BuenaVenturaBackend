package com.buenaventura.erp.recepciones.entity;

import com.buenaventura.erp.compras.entity.Compra;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_recepciones")
public class Recepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdRecepciones")
    private Integer idRecepciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCompras", nullable = false)
    private Compra compra;

    @Column(name = "Recibido", nullable = false, precision = 10, scale = 2)
    private BigDecimal recibido;

    @Column(name = "FechaRecepcion", nullable = false)
    private LocalDateTime fechaRecepcion;

    @Column(name = "GuiaRemision", length = 100)
    private String guiaRemision;

    @Column(name = "TipoEnvase", length = 50)
    private String tipoEnvase;

    @Column(name = "CantidadEnvase", nullable = false)
    private Integer cantidadEnvase;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    public Recepcion() {
    }

    public Integer getIdRecepciones() {
        return idRecepciones;
    }

    public void setIdRecepciones(Integer idRecepciones) {
        this.idRecepciones = idRecepciones;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public BigDecimal getRecibido() {
        return recibido;
    }

    public void setRecibido(BigDecimal recibido) {
        this.recibido = recibido;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
