# Multi-stage Dockerfile: build with Maven, run with a minimal JRE
FROM maven:3.9.11-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the fat jar / built artifact
COPY --from=builder /workspace/target/tic-tac-toe-1.0.0.jar ./app.jar
# Expose a port in case future server features are added (not used by JavaFX UI)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

