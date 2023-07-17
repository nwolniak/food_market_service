FROM maven:3.8.5-eclipse-temurin-17-alpine as base

WORKDIR /app

COPY .mvn .mvn
COPY pom.xml .
COPY mvnw .
RUN ./mvnw clean install

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]