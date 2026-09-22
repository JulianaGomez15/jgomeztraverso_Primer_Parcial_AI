package org.example;

import org.example.dto.ServicioDto;
import org.example.model.CentroServicio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.example.repository.CentroServicioRepository;
import org.example.services.ServicioServices;

import java.math.BigDecimal;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final CentroServicioRepository centroServicioRepository;
    private final ServicioServices servicioServices;

    public DemoDataLoader(CentroServicioRepository centroServicioRepository, ServicioServices servicioServices) {
        this.centroServicioRepository = centroServicioRepository;
        this.servicioServices = servicioServices;
    }

    @Override
    public void run(String... args) {

        // (a) Centro válido existente
        CentroServicio centro = new CentroServicio(
                "30711112223", "Lubricentro Sur", "contacto@lubricentrosur.com", "Av. Siempre Viva 123");
        centroServicioRepository.save(centro);

        // (b) y (c) registro exitoso + persistencia de la relación
        ServicioDto alta = new ServicioDto(
                "30711112223", "Cambio de aceite", "Cambio de aceite y filtro", 30, new BigDecimal("15000"));
        System.out.println("Servicio creado: " + servicioServices.registrarServicio(alta));

        // (d) rechazo por regla de negocio inválida (precio <= 0)
        try {
            ServicioDto altaInvalida = new ServicioDto(
                    "30711112223", "Diagnóstico", "Diagnóstico general", 20, BigDecimal.ZERO);
            servicioServices.registrarServicio(altaInvalida);
        } catch (IllegalArgumentException e) {
            System.out.println("Rechazo esperado: " + e.getMessage());
        }
    }
}
