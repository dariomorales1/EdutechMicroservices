# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY .. /app
# Copia el proyecto completo

WORKDIR /app/supportservice
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=builder /app/supportservice/target/*.jar app.jar
COPY --from=builder /app/supportservice/Wallet_EduTechDB /app/Wallet_EduTechDB

RUN chmod -R 755 /app/Wallet_EduTechDB

ENV TNS_ADMIN=/app/Wallet_EduTechDB

ENTRYPOINT ["java", "-jar", "app.jar"]
