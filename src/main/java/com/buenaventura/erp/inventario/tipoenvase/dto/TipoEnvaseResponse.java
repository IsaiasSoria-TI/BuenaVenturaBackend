package com.buenaventura.erp.inventario.tipoenvase.dto;

public class TipoEnvaseResponse {

    private Integer idTipoEnvase;
    private String nombre;
    private String estado;

    public TipoEnvaseResponse() {
    }

    public Integer getIdTipoEnvase() {
        return idTipoEnvase;
    }

    public void setIdTipoEnvase(Integer idTipoEnvase) {
        this.idTipoEnvase = idTipoEnvase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
