FROM node:20-alpine AS frontend-build

ARG FRONTEND_REPOSITORY=https://github.com/paola31/Agroconecta.git
ARG FRONTEND_REF=main

RUN apk add --no-cache git
RUN git clone --depth 1 --branch "${FRONTEND_REF}" "${FRONTEND_REPOSITORY}" /frontend
WORKDIR /frontend
RUN npm ci
RUN npm run build

FROM maven:3.9.9-eclipse-temurin-17 AS backend-build

WORKDIR /build
COPY pom.xml ./
RUN mvn -B dependency:go-offline
COPY src ./src
COPY --from=frontend-build /frontend/dist ./src/main/resources/static
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre-jammy

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update \
    && apt-get install -y --no-install-recommends mysql-server ca-certificates \
    && rm -rf /var/lib/apt/lists/* /var/lib/mysql/*

WORKDIR /app
COPY --from=backend-build /build/target/agroconecta-back-0.0.1-SNAPSHOT.jar /app/agroconecta.jar
COPY database/init/01-agroconecta.sql /app/database/01-agroconecta.sql
COPY docker/cloud-run-entrypoint.sh /app/cloud-run-entrypoint.sh

RUN chmod 755 /app/cloud-run-entrypoint.sh \
    && mkdir -p /var/lib/mysql /run/mysqld \
    && chown -R mysql:mysql /var/lib/mysql /run/mysqld

ENV PORT=8080 \
    DB_NAME=Agroconecta \
    DB_USER=agro_backend \
    DB_PASSWORD=agro_backend_demo \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=45.0 -XX:InitialRAMPercentage=20.0"

EXPOSE 8080
ENTRYPOINT ["/app/cloud-run-entrypoint.sh"]
