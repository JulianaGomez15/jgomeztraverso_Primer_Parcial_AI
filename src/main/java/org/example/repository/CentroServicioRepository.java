package org.example.repository;

import org.example.model.CentroServicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentroServicioRepository extends JpaRepository<CentroServicio, Long> {

    CentroServicio findByCuit(String cuit);
}
