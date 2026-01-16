# syntax=docker/dockerfile:1

## Build stage: compile the Spring Boot app
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy only the pom first to leverage Docker layer caching for dependencies
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# Copy the source and build
COPY src ./src
ARG MAVEN_ARGS=""
RUN mvn -DskipTests package ${MAVEN_ARGS}

## Runtime stage: slim JRE image
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Copy the fat/boot jar produced by Spring Boot repackage
COPY --from=builder /app/target/*-SNAPSHOT.jar /app/app.jar

# App port (see server.port in application.properties)
EXPOSE 8081

# Tuning defaults (override with --env JAVA_OPTS="...")
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

# Spring profile can be overridden at runtime
ENV SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
