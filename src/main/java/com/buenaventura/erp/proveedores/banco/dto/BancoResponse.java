package com.buenaventura.erp.proveedores.banco.dto;

public class BancoResponse {
    private Integer idBanco;
    private String nombre;
    private Boolean flgActivo;

    public BancoResponse() {
    }

    public BancoResponse(Integer idBanco, String nombre, Boolean flgActivo) {
        this.idBanco = idBanco;
        this.nombre = nombre;
        this.flgActivo = flgActivo;
    }

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
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
