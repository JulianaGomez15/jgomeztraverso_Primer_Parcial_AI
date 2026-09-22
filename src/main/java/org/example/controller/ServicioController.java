package org.example.controller;
import org.example.dto.ServicioDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.services.ServicioServices;

@RestController
@RequestMapping("/v1/servicios")
/*  Post  http://localhost:8080/v1/servicios
 *  Entrada
 *  {
 *    "cuitCentro": "30711112223",
 *    "nombreServicio": "Cambio de aceite",
 *    "descripcion": "Cambio de aceite y filtro",
 *    "duracionMinutos": 30,
 *    "precio": 15000
 *  }
 *  Respuesta: 201 Created + ServicioDto
 *  Si el CUIT no corresponde a un Centro existente: 404 Not Found
 */
public class ServicioController {

    private final ServicioServices servicioServices;

    public ServicioController(ServicioServices servicioServices) {
        this.servicioServices = servicioServices;
    }

    @PostMapping
    public ResponseEntity<ServicioDto> registrarServicio(@RequestBody ServicioDto servicioDto) {
        ServicioDto creado = servicioServices.registrarServicio(servicioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}