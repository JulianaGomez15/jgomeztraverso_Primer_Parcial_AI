package org.example.repository;

import org.example.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByPrecioBetween(BigDecimal precioMinimo, BigDecimal precioMaximo);
}