package org.example.model;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Integer duracionMinutos;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @ManyToOne
    @JoinColumn(name = "centro_id", nullable = false)
    private CentroServicio centro;


    public Servicio(String nombre, String descripcion, Integer duracionMinutos, BigDecimal precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
        this.precio = precio;
    }

    public void setCentro(CentroServicio centro) {
        this.centro = centro;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
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

    public CentroServicio getCentro() {
        return centro;
    }

    @Override
    public String toString() {
        return "Servicio{id=" + id + ", nombre='" + nombre + "', precio=" + precio + "}";
    }
}