package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "centros_servicio")
public class CentroServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cuit;

    @Column(nullable = false)
    private String razonSocial;

    @Column(nullable = false)
    private String email;

    private String direccion;

    @OneToMany(mappedBy = "centro", fetch = FetchType.LAZY)
    private List<Servicio> servicios = new ArrayList<>();


    public CentroServicio(String cuit, String razonSocial, String email, String direccion) {
        this.cuit = cuit;
        this.razonSocial = razonSocial;
        this.email = email;
        this.direccion = direccion;
    }
    public CentroServicio() {}

    public void agregarServicio(Servicio servicio) {
        servicios.add(servicio);
        servicio.setCentro(this);

    }

    public Long getId() {
        return id;
    }

    public String getCuit() {
        return cuit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    @Override
    public String toString() {
        return "CentroServicio{id=" + id + ", cuit='" + cuit + "', razonSocial='" + razonSocial + "'}";
    }
}