# EduTechMicroservices

Plataforma educativa basada en arquitectura de microservicios con Java y Spring Boot. Cada servicio cumple una responsabilidad específica. Actualmente no se ha integrado el frontend.

🔗 **Repositorio GitHub**:  
[https://github.com/dariomorales1/EdutechMicroservices.git](https://github.com/dariomorales1/EdutechMicroservices.git)

## 🏗️ Estructura del Proyecto

```
EduTechMicroservices/
├── authservice/
├── userservice/
├── courseservice/
├── enrollmentservice/
├── evaluationservice/
├── billingservice/
├── supportservice/
├── apigateway/
├── frontend/              # (Planificado)
├── scripts/
└── docker-compose.yml
```

## 🔧 Stack Tecnológico

- **Java 17 + Spring Boot 3.4.x**
- **Spring Security + JWT**
- **WebClient** para llamadas entre servicios
- **PostgreSQL / Oracle** según servicio
- **Docker + Docker Compose**
- **JUnit 5 + Mockito** para pruebas

## 🧩 Microservicios

| Servicio           | Descripción                                        |
|--------------------|----------------------------------------------------|
| authservice        | Login y emisión de JWT                             |
| userservice        | Gestión de usuarios y validación                   |
| courseservice      | Administración de cursos                           |
| enrollmentservice  | Inscripción de usuarios                            |
| evaluationservice  | Registro de evaluaciones                           |
| billingservice     | Facturación y reportes                             |
| supportservice     | Gestión de tickets de soporte                      |
| apigateway         | Entrada centralizada a los servicios               |

## 📁 Estructura Estándar de Cada Servicio

```
src/main/java/cl.edutech.[servicio]/
├── config/
├── controller/
├── DTO/
├── exception/
├── filter/
├── model/
├── repository/
├── service/
├── util/
└── Application.java
```

## 🔒 Seguridad

- Todos los servicios usan JWT y filtros personalizados.
- Autenticación con `AuthService`, validación con `JwtRequestFilter`.

## 🧪 Pruebas

- JUnit 5 + Mockito
- Carpeta `test` en cada microservicio

## 🚀 Modos de Ejecución

### 🔁 Modo Docker

> Para levantar el sistema en Docker:

```bash
docker-compose up --build
```

Antes de ejecutar, realizar estos cambios:

1. **WebClientConfig (en cada servicio)**:
    - Descomentar la URL de Docker (comentada por defecto).
    - Comentar la URL local (`localhost`).

2. **apigateway (`application.yml`)**:
    - Renombrar `application.yml_docker` → `application.yml`
    - Renombrar `application.yml_local` para mantenerlo aparte.

3. **application.properties (por servicio)**:
    - Comentar la línea de conexión local.
    - Descomentar la URL de la base de datos en Docker.

---

### 🧪 Modo Local (desarrollo)

- Comentar las URLs Docker.
- Activar las configuraciones `localhost` en `WebClientConfig` y `application.properties`.
- Ejecutar cada servicio desde tu IDE.

---

## ⚠️ Estado del Proyecto

- ✅ Backend funcional
- 🔐 Seguridad con JWT
- 🔄 Comunicación entre microservicios por WebClient
