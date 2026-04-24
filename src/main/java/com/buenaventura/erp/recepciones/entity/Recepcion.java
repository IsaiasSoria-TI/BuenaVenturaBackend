package com.buenaventura.erp.recepciones.entity;

import com.buenaventura.erp.compras.entity.Compra;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}