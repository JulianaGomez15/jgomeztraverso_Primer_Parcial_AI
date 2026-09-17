# Primer Parcial - Aplicaciones Interactivas (UADE)

Template base de proyecto Spring Boot para el parcial de la materia. Incluye
el `pom.xml`, el `application.properties` y la estructura de paquetes ya
configurados, siguiendo las convenciones usadas en clase (sin Lombok,
constructor `protected` vacío en entidades, reglas de negocio resueltas
desde el Service, `jakarta.transaction.Transactional`, Repositories con
queries derivadas, DTOs planos).

## Stack

- Spring Boot 3.5.16 (`spring-boot-starter-parent`)
- Java 25
- Spring Data JPA + H2 (in-memory)
- Spring Web

## Estructura de paquetes (`org.example`)

```
src/main/java/org/example/
├── Main.java
├── model/        # Entidades JPA
├── dto/          # DTOs de entrada/salida
├── repository/   # Interfaces JpaRepository (sin lógica de negocio)
├── services/     # Casos de uso, validaciones y transaccionalidad
├── controller/   # Exposición REST (bono)
└── config/       # Configuración, DemoDataLoader, etc.
```

Los paquetes están vacíos (con `.gitkeep`) a la espera del dominio que se
defina en el enunciado del parcial.

## Cómo correr

```
mvn spring-boot:run
```

- App: `http://localhost:8089`
- H2 console: `http://localhost:8089/h2-console` (JDBC URL: `jdbc:h2:mem:parcialai`, user: `sa`, password vacío)
