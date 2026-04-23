package com.buenaventura.erp.categoria.dto;

public class CategoriaResponse {

    private Integer idCategoria;
    private String descripcion;
    private Integer idCuentaContable;
    private String codigoCuentaContable;
    private String estado;

    public CategoriaResponse() {
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(Integer idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public String getCodigoCuentaContable() {
        return codigoCuentaContable;
    }

    public void setCodigoCuentaContable(String codigoCuentaContable) {
        this.codigoCuentaContable = codigoCuentaContable;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}