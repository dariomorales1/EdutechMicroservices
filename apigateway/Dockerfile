# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY .. /app
# Copia el proyecto completo

WORKDIR /app/apigateway
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=builder /app/apigateway/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]