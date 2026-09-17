# Convenciones de la cátedra (Aplicaciones Interactivas - UADE)

Cheat sheet extraído del código de referencia del docente (dominio
DeliveryGo). Usar como checklist rápido durante el parcial: reemplazar los
nombres genéricos por las entidades del enunciado, manteniendo el patrón.

## Reglas generales

- **Sin Lombok.** Getters/setters explícitos.
- **DTOs planos**, no records. Van en `dto/`.
- **Sin excepciones custom.** Se usa `IllegalArgumentException` para toda
  violación de regla de negocio.
- **Transaccionalidad:** `jakarta.transaction.Transactional` (¡no
  `org.springframework.transaction.annotation.Transactional`!).
- Las **reglas de negocio se validan en el Service**, nunca en el
  Repository ni en el Controller.
- El `DemoDataLoader` **siempre pasa por los Services**, nunca llama a un
  Repository directamente.

## Entity (`model/`)

```java
@Entity
@Table(name = "nombre_tabla")
public class MiEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String campoUnico;

    @Column(nullable = false)
    private String campoObligatorio;

    // 1:N -> LAZY explícito, sin cascade
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY)
    private List<Hijo> hijos = new ArrayList<>();

    // N:1 -> sin fetch explícito (default EAGER)
    @ManyToOne
    @JoinColumn(name = "padre_id", nullable = false)
    private Padre padre;

    // Constructor vacío OBLIGATORIO: Hibernate instancia por reflexión
    // antes de poblar los campos.
    protected MiEntidad() {
    }

    public MiEntidad(String campoUnico, String campoObligatorio) {
        this.campoUnico = campoUnico;
        this.campoObligatorio = campoObligatorio;
    }

    // getters (y setters solo donde el negocio los necesite)

    @Override
    public String toString() {
        return "MiEntidad{id=" + id + ", campoUnico='" + campoUnico + "'}";
    }
}
```

Método de sincronización bidireccional en el lado "1" de una relación
1:N (ver `CentroServicio.agregarServicio` del simulacro):

```java
public void agregarHijo(Hijo hijo) {
    hijos.add(hijo);
    hijo.setPadre(this);
}
```

## DTO (`dto/`)

```java
public class MiEntidadDto {

    private String campoUnico;
    private String campoObligatorio;

    public MiEntidadDto(String campoUnico, String campoObligatorio) {
        this.campoUnico = campoUnico;
        this.campoObligatorio = campoObligatorio;
    }

    public String getCampoUnico() { return campoUnico; }
    public String getCampoObligatorio() { return campoObligatorio; }

    @Override
    public String toString() {
        return "MiEntidadDto{campoUnico='" + campoUnico + "'}";
    }
}
```

## Repository (`repository/`)

Solo *finders* derivados. Cero lógica de negocio. Devolver la entidad
directa y nullable cuando se busca por una clave de negocio (no
`Optional`); usar los métodos propios de `JpaRepository`
(`findById`, `existsBy...`, etc.) para el resto.

```java
public interface MiEntidadRepository extends JpaRepository<MiEntidad, Long> {
    MiEntidad findByCampoUnico(String campoUnico);
    List<MiEntidad> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByEmail(String email);
}
```

## Service (`services/`)

```java
@Service
public class MiEntidadServices {

    private final MiEntidadRepository miEntidadRepository;

    public MiEntidadServices(MiEntidadRepository miEntidadRepository) {
        this.miEntidadRepository = miEntidadRepository;
    }

    @Transactional
    public MiEntidadDto crear(MiEntidadDto dto) {

        if (Objects.nonNull(miEntidadRepository.findByCampoUnico(dto.getCampoUnico()))) {
            throw new IllegalArgumentException("Ya existe: " + dto.getCampoUnico());
        }

        MiEntidad entidad = validar(dto);
        miEntidadRepository.save(entidad);
        return dto;
    }

    // Validaciones de negocio en método PRIVADO del Service.
    private MiEntidad validar(MiEntidadDto dto) {

        String campo = dto.getCampoObligatorio();
        if (Objects.isNull(campo) || campo.isEmpty()) {
            throw new IllegalArgumentException("El campo no puede ser nulo");
        }

        return new MiEntidad(dto.getCampoUnico(), campo);
    }
}
```

Para relaciones donde "el padre debe existir previamente" (ej.
`ServicioServices.registrarServicio` del simulacro): buscar el padre por
su clave de negocio, validar `Objects.isNull(...)`, y recién ahí construir
y asociar el hijo antes de persistir.

## Controller (bono) (`controller/`)

```java
@RestController
@RequestMapping("/v1/mi-entidad")
public class MiEntidadController {

    private final MiEntidadServices miEntidadServices;

    public MiEntidadController(MiEntidadServices miEntidadServices) {
        this.miEntidadServices = miEntidadServices;
    }

    @PostMapping
    public ResponseEntity<MiEntidadDto> crear(@RequestBody MiEntidadDto dto) {
        MiEntidadDto creado = miEntidadServices.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
```

## DemoDataLoader (`config/`)

```java
@Configuration
public class DemoDataLoader {

    @Bean
    CommandLineRunner demo(MiEntidadServices miEntidadServices /*, otros Services */) {

        return args -> {

            MiEntidadDto creado = miEntidadServices.crear(
                    new MiEntidadDto("valor-unico", "obligatorio"));

            System.out.println("\n=== INTENTOS INVALIDOS (deben rechazarse) ===");
            try {
                miEntidadServices.crear(new MiEntidadDto("valor-unico", "obligatorio"));
            } catch (IllegalArgumentException e) {
                System.out.println("Rechazo esperado (duplicado): " + e.getMessage());
            }

            System.out.println("\nH2 console: http://localhost:8089/h2-console");
        };
    }
}
```

## Checklist de trazabilidad antes de entregar

- [ ] El Caso de Uso, el Diagrama de Clases, el DER y el código representan
      **la misma solución** (mismos nombres de entidades/atributos en los
      cuatro artefactos).
- [ ] Toda regla de negocio del enunciado tiene su validación explícita en
      un Service (no en la Entity ni en el Repository).
- [ ] Relaciones obligatorias (`X debe existir para crear Y`) validan
      `Objects.isNull(...)` antes de persistir.
- [ ] Restricciones de unicidad/negocio también están reflejadas a nivel
      de base (`unique = true`, `nullable = false` en la FK).
- [ ] El método `@Transactional` del Service es el que orquesta toda la
      operación (todo o nada).
