package com.buenaventura.erp.proveedores.banco.dto;

public class BancoResponse {
    private Integer idBanco;
    private String nombre;

    public BancoResponse() {
    }

    public BancoResponse(Integer idBanco, String nombre) {
        this.idBanco = idBanco;
        this.nombre = nombre;
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
}