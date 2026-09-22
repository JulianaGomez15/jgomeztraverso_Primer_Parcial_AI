package org.example.services;
import org.example.dto.ServicioDto;
import jakarta.transaction.Transactional;
import org.example.model.CentroServicio;
import org.example.model.Servicio;
import org.springframework.stereotype.Service;
import org.example.repository.CentroServicioRepository;
import org.example.repository.ServicioRepository;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class ServicioServices {

    private final ServicioRepository servicioRepository;
    private final CentroServicioRepository centroServicioRepository;

    public ServicioServices(ServicioRepository servicioRepository,
                            CentroServicioRepository centroServicioRepository) {
        this.servicioRepository = servicioRepository;
        this.centroServicioRepository = centroServicioRepository;
    }

    @Transactional
    public ServicioDto registrarServicio(ServicioDto servicioDto) {

        CentroServicio centro = centroServicioRepository.findByCuit(servicioDto.getCuitCentro());
        if (Objects.isNull(centro)) {
            throw new IllegalArgumentException(
                    "No existe un Centro de Servicio con CUIT: " + servicioDto.getCuitCentro());
        }

        Servicio servicio = validarServicio(servicioDto);
        centro.agregarServicio(servicio);

        servicioRepository.save(servicio);
        return servicioDto;
    }

    private Servicio validarServicio(ServicioDto dto) {

        String nombre = dto.getNombreServicio();
        if (Objects.isNull(nombre) || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio no puede ser nulo");
        }

        BigDecimal precio = dto.getPrecio();
        if (Objects.isNull(precio) || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del servicio debe ser mayor que cero");
        }

        Integer duracion = dto.getDuracionMinutos();
        if (Objects.isNull(duracion) || duracion <= 0) {
            throw new IllegalArgumentException("La duración del servicio debe ser mayor que cero");
        }

        return new Servicio(nombre, dto.getDescripcion(), duracion, precio);
    }
}