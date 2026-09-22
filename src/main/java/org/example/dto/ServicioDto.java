package org.example.dto;

import java.math.BigDecimal;

public class ServicioDto {

    private String cuitCentro;
    private String nombreServicio;
    private String descripcion;
    private Integer duracionMinutos;
    private BigDecimal precio;

    public ServicioDto(String cuitCentro, String nombreServicio, String descripcion,
                       Integer duracionMinutos, BigDecimal precio) {
        this.cuitCentro = cuitCentro;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
        this.precio = precio;
    }

    public String getCuitCentro() {
        return cuitCentro;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "ServicioDto{nombreServicio='" + nombreServicio + "', precio=" + precio +
                ", cuitCentro='" + cuitCentro + "'}";
    }
}
