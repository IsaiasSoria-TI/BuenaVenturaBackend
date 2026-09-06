package com.buenaventura.erp.articulo.entity;

import com.buenaventura.erp.categoria.entity.Categoria;
import com.buenaventura.erp.inventario.tipoenvase.entity.TipoEnvase;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_articulo")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdArticulo")
    private Integer idArticulo;

    @Column(name = "Descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "Medida", nullable = false, length = 50)
    private String medida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipoEnvase", nullable = false)
    private TipoEnvase tipoEnvase;

    @Column(name = "Stock", nullable = false, precision = 10, scale = 2)
    private BigDecimal stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCategoria", nullable = false)
    private Categoria categoria;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado;

    public Articulo() {
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public TipoEnvase getTipoEnvase() {
        return tipoEnvase;
    }

    public void setTipoEnvase(TipoEnvase tipoEnvase) {
        this.tipoEnvase = tipoEnvase;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
