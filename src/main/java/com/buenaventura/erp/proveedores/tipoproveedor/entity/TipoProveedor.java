package com.buenaventura.erp.proveedores.tipoproveedor.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_tipo_proveedor")
public class TipoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipoProveedor")
    private Integer idTipoProveedor;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    public TipoProveedor() {
    }

    public Integer getIdTipoProveedor() {
        return idTipoProveedor;
    }

    public void setIdTipoProveedor(Integer idTipoProveedor) {
        this.idTipoProveedor = idTipoProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }
}