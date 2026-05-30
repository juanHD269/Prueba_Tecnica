# Prueba Técnica — API de Alquiler de Bicicletas (Spring Boot)

## Requisitos
- Java 17
- Maven 3.9+

## Cómo ejecutar

### 1) Levantar la API
```bash
./mvnw spring-boot:run
```
En Windows:
```bash
.\mvnw.cmd spring-boot:run
```

La API queda en:
- http://localhost:8080

## Despliegue (opcional)
El despliegue en nube no es requerido por el enunciado, pero se puede usar como diferenciador.

La API ya soporta `PORT` por variable de entorno (útil en plataformas como Render):
- `server.port=${PORT:8080}`

### Render (recomendado)
Configurar el servicio como “Docker” y dejar que Render use el `Dockerfile` del repositorio.

### 2) Consola H2 (opcional)
- http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:bikerental`
- user: `sa`
- password: (vacío)

### 3) Ejecutar pruebas
```bash
./mvnw test
```

## Seguridad básica
- Endpoints GET bajo `/api/**`: públicos
- Endpoints que modifican estado (POST bajo `/api/**`): requieren Basic Auth

Credenciales por defecto (configurables en `application.properties`):
- usuario: `admin`
- contraseña: `admin`

## Endpoints principales

### Registrar bicicleta (RF-01)
`POST /api/bicycles` (requiere auth)

Body:
```json
{
  "code": "BIC-010",
  "type": "MONTAÑA",
  "status": "DISPONIBLE"
}
```

Notas:
- `type` acepta `URBANA`, `MONTAÑA`/`MONTANA`, `ELÉCTRICA`/`ELECTRICA`
- `status` es opcional (por defecto `DISPONIBLE`)

### Consultar disponibilidad (RF-04)
`GET /api/bicycles/available`

Filtro opcional:
`GET /api/bicycles/available?type=URBANA`

### Iniciar alquiler (RF-02)
`POST /api/rentals` (requiere auth)

Body:
```json
{
  "bicycleCode": "BIC-001",
  "clientName": "Juan",
  "estimatedDurationHours": 2
}
```

Regla:
- Solo bicicletas en `DISPONIBLE` pueden alquilarse. Al iniciar, la bicicleta pasa a `ALQUILADA`.

### Finalizar alquiler (RF-03)
`POST /api/rentals/{id}/finish` (requiere auth)

Reglas:
1. Registra hora real de devolución (hora del servidor).
2. Calcula costo base por tiempo real redondeado hacia arriba a horas completas.
3. Aplica multa si el tiempo real supera la duración estimada.
4. Cambia el estado de la bicicleta a `DISPONIBLE`.

### Historial de alquileres de una bicicleta (RF-05)
`GET /api/bicycles/{code}/rentals`

Devuelve lista con: cliente, hora inicio, hora fin (si terminó), duración real (minutos), costo total y si tuvo multa.

## Reglas de negocio implementadas

### RN-01 — Tarifas por tipo (por hora)
- URBANA: 3500
- MONTAÑA: 5000
- ELÉCTRICA: 7500

### RN-02 — Costo base
- Se calcula con el tiempo real (fin - inicio) redondeado al alza a horas completas.
- Ejemplos:
  - 1h 10min ⇒ 2 horas facturables
  - 2h exactas ⇒ 2 horas facturables

### RN-03 — Multa por devolución tardía
- Si `tiempoReal > estimado`, se cobra multa del 50% de la tarifa/hora por cada hora de retraso.
- El retraso también se redondea al alza, y el mínimo facturable es 1 hora si existe retraso.

### RN-04 — No se puede alquilar una bicicleta no disponible
- Si el estado no es `DISPONIBLE`, la API responde `409 Conflict` con mensaje descriptivo.

### RN-05 — No se puede finalizar un alquiler inexistente o ya finalizado
- `404 Not Found` si el alquiler no existe
- `409 Conflict` si el alquiler ya fue finalizado

## Datos de ejemplo
Al iniciar la app se cargan bicicletas de referencia en H2 (ver `src/main/resources/data.sql`):
- BIC-001 URBANA DISPONIBLE
- BIC-002 MONTAÑA DISPONIBLE
- BIC-003 ELÉCTRICA DISPONIBLE
- BIC-004 MONTAÑA EN_MANTENIMIENTO
- BIC-005 URBANA DISPONIBLE

## Arquitectura (resumen)
- `bicycle`: entidad + repositorio + servicio para gestión y consulta de disponibilidad.
- `rental`: entidad + repositorio + servicio con reglas de negocio del flujo de alquiler y cálculo de costos/multas.
- `api`: controladores REST + DTOs (requests/responses) + manejo de errores.
- `config`: configuración de `Clock` (para testear tiempos de forma determinista) y seguridad básica (Basic Auth).

Decisiones relevantes:
- Se usa `Clock` inyectable para poder probar el cálculo de duración/costos sin depender del tiempo real.
- Se persiste el resultado del cálculo (costo base, multa, total, horas) al finalizar, para que el historial sea consistente.

## Ejemplos con curl

### Iniciar alquiler (con auth)
```bash
curl -u admin:admin -H "Content-Type: application/json" -d "{\"bicycleCode\":\"BIC-001\",\"clientName\":\"Juan\",\"estimatedDurationHours\":2}" http://localhost:8080/api/rentals
```

### Finalizar alquiler (con auth)
```bash
curl -u admin:admin -X POST http://localhost:8080/api/rentals/1/finish
```

### Consultar disponibilidad
```bash
curl http://localhost:8080/api/bicycles/available
```
