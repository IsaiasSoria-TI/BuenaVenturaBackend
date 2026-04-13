package com.buenaventura.erp.persona.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPersona")
    private Integer idPersona;

    @Column(name = "Nombres", nullable = false, length = 120)
    private String nombres;

    @Column(name = "ApellidoPaterno", nullable = false, length = 35)
    private String apellidoPaterno;

    @Column(name = "ApellidoMaterno", nullable = false, length = 35)
    private String apellidoMaterno;

    @Column(name = "Telefono", nullable = false, length = 9)
    private String telefono;

    @Column(name = "DNI", nullable = false, length = 8)
    private String dni;

    @Column(name = "Correo", length = 150)
    private String correo;

    @Column(name = "FlgActivo")
    private Boolean flgActivo;

    public Persona() {
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(Boolean flgActivo) {
        this.flgActivo = flgActivo;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidoPaterno + " " + apellidoMaterno;
    }
}